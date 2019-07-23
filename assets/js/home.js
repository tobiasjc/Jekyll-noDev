function hider(e, val) {
    if (val == true) {
        e.css("visibility", "visible");
        e.fadeIn();
    } else {
        e.fadeOut();
    }
}

var point = $("#nav-full");
var rollHead = $("#on-roll-head");
var rollFoot = $("#on-roll-foot");
var hh = $(window).scrollTop();

if (hh > point.height()) {
    hider(rollHead, true);
    hider(rollFoot, true);
} else {
    hider(rollHead, false);
    hider(rollFoot, false);
}

$(window).scroll(function () {
    var hh = $(window).scrollTop();
    if (hh > point.height()) {
        hider(rollHead, true);
        hider(rollFoot, true);
    } else {
        hider(rollHead, false);
        hider(rollFoot, false);
    }
});
