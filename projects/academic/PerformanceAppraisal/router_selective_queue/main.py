import heapq
import random

import dispatcher as dsp
import entities
import generators as gens
import io_management as IO


if __name__ == "__main__":
    # set seed before any operation
    random.seed(1000)

    # read user's input
    [sp, ep, val] = IO.user_setup()

    # setup two normal queues: one for web packets, one for CBR packets
    web_queue = []
    cbr_queue = []

    # setup the priority queue of events: events that should happen first, are going to happen first
    events = []
    heapq.heapify(events)

    # setup two initial events: web packet arrival and VoIP connection arrival, and push them to the events queue
    initial_event_web = entities.EventArrival(ep, sp, entities._WEB)
    initial_event_web.next_event_update()
    heapq.heappush(events, initial_event_web)

    initial_event_voip = entities.EventArrival(ep, sp, entities._VOIP)
    initial_event_voip.next_event_update()
    heapq.heappush(events, initial_event_voip)

    # simulation start #
    while ep.time_actual <= sp.simulation_duration:
        # event that is happening at the moment (top of the queue)
        happening = heapq.heappop(events)

        # case 1: web packet arrival
        if happening.case == entities._WEB:
            ep.time_actual = happening.next_event
            happening.next_event_update()
            heapq.heappush(events, happening)

            packet = entities.Packet(ep.time_actual, gens.gen_packet_size())
            web_queue.append(packet)

            # if there is no one waiting and the router is not working, attend the packet immediately
            if len(cbr_queue) == 0 and not ep.router_working:
                service = entities.EventService(entities._SERVICE, ep.time_actual)
                heapq.heappush(events, service)

        # case 2: VoIP connection arrival
        elif happening.case == entities._VOIP:
            ep.time_actual = happening.next_event
            happening.next_event_update()
            heapq.heappush(events, happening)

            duration = gens.gen_exp(sp.voip_connection_duration)
            cbr_interval = gens.gen_cbr_interval()

            conn = entities.EventConnection(ep, sp, entities._CBR, duration, cbr_interval)
            conn.next_event_update()
            heapq.heappush(events, conn)

            cbr = entities.Packet(ep.time_actual, sp.cbr_packet_size)
            cbr_queue.append(cbr)

            # if there is no one waiting and the router is not working, attend the packet immediately
            if len(web_queue) == 0 and not ep.router_working:
                service = entities.EventService(entities._SERVICE, ep.time_actual)
                heapq.heappush(events, service)

        # case 3: CBR packet from VoIP connection arrival
        elif happening.case == entities._CBR:
            ep.time_actual = happening.next_event
            happening.next_event_update()

            # if connection time is not over, push it back into the events for the next CBR
            if not happening.is_over():
                heapq.heappush(events, happening)

            cbr = entities.Packet(ep.time_actual, sp.cbr_packet_size)
            cbr_queue.append(cbr)

            # if there is no one waiting and the router is not working, attend the packet immediately
            if len(web_queue) == 0 and not ep.router_working:
                service = entities.EventService(entities._SERVICE, ep.time_actual)
                heapq.heappush(events, service)

        # case 4: system's service attendance
        elif happening.case == entities._SERVICE:
            ep.time_actual = happening.next_event
            
            # utilization update
            if ep.time_actual == ep.time_packet_exit:
                val.exit(ep.time_actual)
            else:
                val.enter(ep.time_actual)
            
            # both queues are empty, now router stops working
            if len(web_queue) == 0 and len(cbr_queue) == 0:
                ep.router_working = False
                
            # only web queue has members, then router receives its service: a web packet
            elif len(web_queue) == 0:
                dsp.packet_dispatch(cbr_queue, ep, sp, val)
                service = entities.EventService(
                    entities._SERVICE, ep.time_packet_exit)
                heapq.heappush(events, service)
            
            # only CBR queue has members, then router receives its service: a CBR queue
            elif len(cbr_queue) == 0:
                dsp.packet_dispatch(web_queue, ep, sp, val)
                service = entities.EventService(
                    entities._SERVICE, ep.time_packet_exit)
                heapq.heappush(events, service)
            
            # both queues have members, then the system should balance its attendance
            else:
                dsp.balance_dispatch(web_queue, cbr_queue, ep, sp, val)
                service = entities.EventService(entities._SERVICE, ep.time_packet_exit)
                heapq.heappush(events, service)

    # update system's validations
    val.exit(ep.time_actual)
    val.enter(ep.time_actual)
    
    # evaluation validations
    IO.report_validations(val, ep)
    
