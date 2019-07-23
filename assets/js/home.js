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
const triggerPoint = header.height() - rollHead.height();
const entities = [rollHead, rollFoot];

// if the page is reload in the middle
(height >= triggerPoint) ? entities.forEach(shower) : entities.forEach(hider);

// scroll control
$(window).scroll(function () {
    var height = $(window).scrollTop();
    (height >= triggerPoint) ? entities.forEach(shower) : entities.forEach(hider);
});

// scrollback on posts size
var postSize = $("#achievments-list li").first().height();
$("#on-roll-head a.nav-link").on("click", function (e) {
    e.preventDefault();
    $($(this).attr('href'))[0].scrollIntoView();
    scrollBy(0, -postSize);
});

$('body').scrollspy({ target: '#spied-body', offset: 50 });