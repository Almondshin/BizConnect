// 4, 4.1 페이지
let autopay = true;

// 상품선택 리스트 세팅
function setPayment (boolean) {
    // 결제방식을 자동결제 선택유무에 따라 disable 여부 결정
    autopay = boolean;
    let paymentRadioGroup = document.querySelectorAll("input[name=howtopay]");
    let paymentRadioWrapper = document.querySelector(".info-itme-howtopay");
    let autopayText = document.querySelector("#info_text_autopay");
    if (autopay) {
        paymentRadioWrapper.classList.add("active");
        autopayText.innerText = "사용";
        paymentRadioGroup.forEach((elemnt) => {
            elemnt.disabled = true;
        });
        document.querySelector("#creditcard").checked = true;
    }
    else {
        paymentRadioWrapper.classList.remove("active");
        autopayText.innerText = "사용하지않음";
        paymentRadioGroup.forEach((elemnt) => {
            elemnt.disabled = false;
        });
    }
}

function setInformation_4 (obj) {
    let companyName = document.querySelector("#info_company_name");
    let bizNum = document.querySelector("#info_biz_num");
    let userName = document.querySelector("#info_userName");

    if (obj) {
        companyName.innerText = obj.companyName;
        bizNum.innerText = obj.bizNum;
        userName.innerText = obj.userName;
        // autopay = obj.autoPay;
        setPayment(obj.autoPay);
    }
    else {
        companyName.innerText = "(주)드림컴퍼니";
        bizNum.innerText = "748-45-007911";
        userName.innerText = "김대표";
        // autopay = false;
        setPayment(obj.autoPay);
    }
}

function setProductTable (objArray) {
    // 상품 변경모달내부 세팅
    let productList = document.querySelector(".product_list");
    productList.innerHTML = "";
    objArray.forEach((itm, index) => {
        let productItem = document.createElement("tr");
        productItem.classList.add("product_item");

        // productItem.innerHTML = "<td><input type='radio' name='product_select' id='' /></td>";
        // productItem.innerHTML += "<td class='product_name'>" + itm.productName + "</td>";
        // productItem.innerHTML += "<td class='product_price'>" + itm.productPrice + "</td>";
        // productItem.innerHTML += "<td class='product_count'>" + itm.productCount + "</td>";
        // productItem.innerHTML += "<td class='product_feePerCase'>" + itm.productFeePerCase + "</td>";
        // productItem.innerHTML += "<td class='product_ExcessFeePerCase'>" + itm.productExcessFeePerCase + "</td>";
        let productItemId = "product" + index;
        productItem.innerHTML = `
            <td><input type='radio' name='product_select' id='${productItemId}' data-product-id="${itm.type}" data-product-index="${index}" /></td>
            <td class='product_name'>${itm.productName}</td>
            <td class='product_price'>${itm.productPrice}</td>
            <td class='product_count'>${itm.productCount}</td>
            <td class='product_feePerCase'>${itm.productFeePerCase}</td>
            <td class='product_ExcessFeePerCase'>${itm.productExcessFeePerCase}</td>
        `
        productList.append(productItem);
    });
    // 정기결제 toggle버튼 이벤트
}

let toggleBtnMonthlyAutoPay = document.querySelector("#toggle_btn_monthlyautopay");
if (autopay) {
    toggleBtnMonthlyAutoPay.classList.add("active");
}
else {
    toggleBtnMonthlyAutoPay.classList.remove("active");
}
toggleBtnMonthlyAutoPay.addEventListener("click", function () {
    toggleBtnMonthlyAutoPay.classList.toggle("active");
    {
        autopay ? setPayment(false) : setPayment(true);
    }
});

function getTodayDate () {
    var today = new Date();
    var year = today.getFullYear();
    var month = String(today.getMonth() + 1).padStart(2, "0");
    var day = String(today.getDate()).padStart(2, "0");
    return year + "-" + month + "-" + day;
}

function payProgress () {
    let datepicker = document.querySelector("#datepicker").value;
    var datePattern = /^\d{4}-\d{2}-\d{2}$/;
    let today = getTodayDate();
    if (datePattern.test(datepicker) && today <= datepicker) {
    }
    else {
        popupOpen(1);
    }
}

function checkDate () {
    let patten = new RegExp("d{4}-d{2}-d{2}");
    let datepicker = document.querySelector("#datepicker");
    let today = getTodayDate();
    if (datepicker.length < 10 && patten.test(datepicker) && today <= datepicker.value) {
    }
}

// 5. thankyou 페이지 등록정보의 내용을 세팅하는 함수
function setInformation_5 (obj) {
    let companyName = document.querySelector("#info_company_name");
    let bizNum = document.querySelector("#info_biz_num");
    let productName = document.querySelector("#info_product_name");
    let autoPay = document.querySelector("#info_autopay");
    let startDate = document.querySelector("#info_start_date");

    if (obj) {
        companyName.innerText = obj.companyName;
        bizNum.innerText = obj.bizNum;
        productName.innerText = obj.productName;
        autoPay.innerText = obj.autoPay;
        startDate.innerText = obj.startDate;
    }
    else {
        companyName.innerText = `(주)드림컴퍼니`;
        bizNum.innerText = `748-45-007911`;
        productName.innerText = `스탠다드(96,000원 / 2000건)`;
        autoPay.innerText = `사용`;
        startDate.innerText = `2024년 3월 1일`;
    }
}

// 공통
// 페이지 반응형
function responsiveLayout () {
    if (window.innerWidth > 768) {
        contentBoxList.forEach((item, index) => {
            item.classList.add("content-grid");
            item.classList.remove("content-flex");
        });
    }
    else {
        contentBoxList.forEach((item, index) => {
            item.classList.add("content-flex");
            item.classList.remove("content-grid");
        });
    }
}

//
window.addEventListener("resize", function () {
    responsiveLayout();
});
