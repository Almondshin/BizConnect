// 4, 4.1 페이지
let autopay = true;

function setHTMLProductName (str) {
    let productName = document.querySelector("#info_product_name");
    productName.innerText = str;
}

function setHTMLProductTotalPrice (str) {
    let productTotalPrice = document.querySelector("#info_totalprice");
    productTotalPrice.innerText = str
}

/**
 * 상품 계산된 가격 표시
 * 개발 리소스는 새로 선언
 * @returns {string}
 */

function calcPrice () {
    let obj = getSelectedProduct()
    let datepickerValue = document.querySelector("#datepicker").value.split("-");
    let year = parseInt(datepickerValue[0]);
    let month = parseInt(datepickerValue[1]);
    let day = parseInt(datepickerValue[2]) - 1;
    let lastDay = new Date(year, month, 0).getDate();
    let price = Math.floor(obj.productPrice * ((lastDay - day) / lastDay));
    let count = Math.floor(obj.productCount * ((lastDay - day) / lastDay));
    return price + "원 (" + count + "건)";
}

/**
 * 선택된 상품명 반환
 * @returns {string|*}
 */
function getProductName () {
    let target = productDatalist.filter(e => {
        return e.productCode === userData.productCode
    })

    return target.length === 0 ? "" : target[0].productName
}

/**
 *  이용기관 정보 세팅
 * @param obj
 */
function setInformation_4 (obj) {
    let companyName = document.querySelector("#info_company_name");
    let bizNum = document.querySelector("#info_biz_num");
    let userName = document.querySelector("#info_userName");

    if (obj) {
        companyName.innerText = obj.companyName;
        bizNum.innerText = obj.bizNum;
        userName.innerText = obj.userName;
    }
    else {
        companyName.innerText = "(주)테스트컴퍼니";
        bizNum.innerText = "748-45-007911";
        userName.innerText = "김대표";
    }
}

function setProductCode (code) {
    userData.productCode = code;
}

function getProductCode () {
    return userData.productCode;
}

/**
 * 상품 테이블 HTML 생성
 * @param objArray
 */
function setProductTable (objArray) {
    // 상품 변경모달내부 세팅
    let productList = document.querySelector(".product_list");
    productList.innerHTML = "";

    objArray.forEach((item, index) => {
        let productItem = document.createElement("tr");
        let checked = false;
        productItem.classList.add("product_item");
        if (item.productAutopay) {
            productItem.classList.add("autopay")
        }
        productItem.innerHTML = `
            <td><input type="radio" name="product_select" id="${item.productCode}" data-index="${index}" /></td>
            <td class='product_name'> ${item.productName} </td>
            <td class='product_price'> ${item.productPrice.toLocaleString()} 원</td>
            <td class='product_count'> ${item.productCount.toLocaleString()} 건</td>
            <td class='product_feePerCase'> ${item.productFeePerCase.toLocaleString()} 원</td>
            <td class='product_ExcessFeePerCase'>${item.productExcessFeePerCase.toLocaleString()} 원</td>
        `
        productList.append(productItem);
    });
}

/**
 *  결제 정보 설정 함수
 *  상품 선택이 되었을 세팅
 */
function setPayment () {
    popupClose();

    let index = document.querySelector("input[name=product_select]:checked").dataset.index
    setProductCode(productDatalist[index].productCode)
    let paymentRadioGroup = document.querySelectorAll("input[name=howtopay]");
    let paymentRadioWrapper = document.querySelector(".info-item-howtopay");
    let autopayText = document.querySelector("#info_text_autopay");

    setHTMLProductName(getProductName());
    setHTMLProductTotalPrice(calcPrice());

    autopay = getSelectedProduct().productAutopay;
    if (autopay) {
        paymentRadioWrapper.classList.add("active");
        autopayText.innerText = "사용";
        paymentRadioGroup.forEach((element) => {
            element.disabled = true;
        });
        document.querySelector("#creditcard").checked = true;
    }
    else {
        paymentRadioWrapper.classList.remove("active");
        autopayText.innerText = "미사용";
        paymentRadioGroup.forEach((element) => {
            element.disabled = false;
        });
    }
}

function selectProduct () {
    Array.prototype.forEach.call(document.querySelectorAll("input[name=product_select]"), function (el) {
        if (el.id === userData.productCode) {
            el.checked = true;
        }
    })
}

// 선택된
function getSelectedProduct () {
    let index = document.querySelector("input[name=product_select]:checked").dataset.index;
    return productDatalist[index];
}

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
