package service;

import java.util.HashMap;

import graphics.ScheduleGUI;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloObjective;
import ilog.cplex.IloCplex;
import management.Constructor;
import management.Environment;

public class App {

	public static void main(String[] args) {
		HashMap<String, String> argsHash = readArgsHash(args);
		Environment env = new Environment(argsHash);

		int P = env.getProfessorQuantity();
		int D = env.getDayQuantity();
		int C = env.getClassQuantity();
		int S = env.getSlotQuantity();

		Constructor constructor = new Constructor(P, D, C, S);

		boolean[][][] i = env.readImpossibilities();
		int[][] o = env.readObligations();

		try {
			IloCplex model = new IloCplex();
//			model.setParam(Param.TimeLimit, 300);
			IloIntVar[][] q = new IloIntVar[P][D];

			// set instance model inside 'x'
			IloIntVar[][][][] x = new IloIntVar[P][D][C][S];
			constructor.setInstanceParameters(model, x, i, o, q);

			/* Restrictions Section */
			constructor.restrictTrippleSlots(model, x);

			/* Quality Settings Sections */
			// objective function to be built
			IloObjective z = model.addMinimize();

			// twined slots maximizer
//			constructor.setTwined(model, x, z);

			// isolated slots minimizer
			constructor.setIsolated(model, x, z);

			// professor window minimizer
			constructor.setWindows(model, x, z, q);

			if (model.solve()) {
				System.out.println(model.getObjective());
				env.showClassSchedule(x, model);
				env.showProfessorSchedule(x, model, q);

				ScheduleGUI scheduleGUI = new ScheduleGUI(model, x, P, D, C, S);
				scheduleGUI.bring();
			} else {
				System.err.println("Not solvable.");
				System.exit(1);
			}
		} catch (IloException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static HashMap<String, String> readArgsHash(String[] args) {
		if (args.length == 0 || args.length % 2 != 0) {
			System.err.println("Wrong number of parameters, they should follow the pattern --<KEY> <VALUE>");
			System.exit(1);
		}

		HashMap<String, String> tmp = new HashMap<String, String>();
		for (int i = 0; i < args.length; i += 2) {
			tmp.put(args[i], args[i + 1]);
		}

		return tmp;
	}

}
