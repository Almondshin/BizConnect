function popupOpen (popNum) {
    popupClose();
    $("body").addClass("stop-scrolling");
    document.querySelector(".container > *").setAttribute("tabIndex", "-1");
    $(".popup_overlay").addClass("active");
    switch (popNum) {
        case 1: {
            $("#popup_cancel").addClass("active");
            break;
        }
        case 2: {
            $("#modal_products").addClass("active");
            setProductTable(page4_1_tableData);
            break;
        }
        case 3: {
            $("#modal_products").addClass("active");
            break;
        }
    }
}

function popupClose (popNum) {
    $("body").removeClass("stop-scrolling");
    $(".popup_overlay").removeClass("active");
    $(".popup_wrap").removeClass("active");
    $(".modal_wrap").removeClass("active");
    switch (popNum) {
        case 1: {
            $("#datepicker").focus();
        }
    }
}
