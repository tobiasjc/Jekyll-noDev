/* helper functions */
function hider(e) {
    e.fadeOut();
}

function shower(e) {
    e.css("visibility", "visible");
    e.fadeIn();
}
/* */

// scroll height marker
var height = $(window).scrollTop();

// entities to be managed
const header = $("#nav-full");
const rollHead = $("#on-roll-head");
const rollFoot = $("#on-roll-foot");
const triggerPoint = header.height() - (rollHead.height());
const entities = [rollHead, rollFoot];

// if the page is reload in the middle
(height > triggerPoint) ? entities.forEach(shower) : entities.forEach(hider);

// scroll control
$(window).scroll(function () {
    var height = $(window).scrollTop();
    (height > triggerPoint) ? entities.forEach(shower) : entities.forEach(hider);
});

// scrollback on posts size
const scrollBackHead = rollHead.height();
const scrollBackItem = $("#achievments-list li.list-group-item").first().height();

const onRollFirst = $("#on-roll-head a.nav-link").first();
const onRollRest = $("#on-roll-head a.nav-link").slice(1, $("#on-roll-head li.nav-item").length);


onRollFirst.on("click", function (e) {
    e.preventDefault();
    $($(this).attr('href'))[0].scrollIntoView();
    scrollBy(0, -scrollBackHead);
});

onRollRest.on("click", function (e) {
    console.log(scrollBackItem);
    e.preventDefault();
    $($(this).attr('href'))[0].scrollIntoView();
    scrollBy(0, -scrollBackItem);
});


// scrollspy activation offset
const offSet = $(window).height() * 0.2;
$('body').scrollspy({ target: '#spied-body', offset: offSet });