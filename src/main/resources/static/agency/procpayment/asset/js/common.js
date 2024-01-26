// 4, 4.1 페이지
let autopay = true;

// 결제정보 세팅
function setPayment () {
    let paymentRadioGroup = document.querySelectorAll("input[name=howtopay]");
    let paymentRadioWrapper = document.querySelector(".info-itme-howtopay");
    let autopayText = document.querySelector("#info_text_autopay");

    setHTMLProductName(getProductName());
    setHTMLProductTotalPrice(calcPrice(getSelectedProduct()));

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

function setHTMLProductName(str){
    let productName = document.querySelector("#info_product_name");
    productName.innerText = str;
}
function setHTMLProductTotalPrice(str){
    let productTotalPrice = document.querySelector("#info_totalprice");
    productTotalPrice.innerText = str
}

// 가격 계산
function calcPrice (obj) {
    console.log("calcPrice 동작");
    let datepickerValue = document.querySelector("#datepicker").value.split("-");

    // 계산 테스트 데이터
    // 계산방식 예시에 있는 희망일 1/3일 기준으로 계산하는 테스트
    // datepickerValue = ["2024", "1", "3"];

    let year = parseInt(datepickerValue[0]);
    let month = parseInt(datepickerValue[1]);
    let day = parseInt(datepickerValue[2]) - 1;
    let lastDay = new Date(year, month, 0).getDate();
    console.log(year, month, day, lastDay);

    let price = Math.floor(obj.productPrice * ((lastDay - day) / lastDay));
    let count = Math.floor(obj.productCount * ((lastDay - day) / lastDay));

    console.log(price);
    console.log(count);

    return price + "원 (" + count + "건)";
}

// 결제 상품명
function getProductName () {
    let target = productDatalist.filter(e => {
        return e.productCode === userData.productCode
    })

    return target.length === 0 ? "" : target[0].productName
}

// 결제페이지 정보 초기세팅
function setInformation_4 (obj) {
    console.log(obj);
    let companyName = document.querySelector("#info_company_name");
    let bizNum = document.querySelector("#info_biz_num");
    let userName = document.querySelector("#info_userName");

    if (obj) {
        companyName.innerText = obj.companyName;
        bizNum.innerText = obj.bizNum;
        userName.innerText = obj.userName;
        // autopay = obj.autoPay;
        // productName.innerText = getProductName(obj.productName);
        // setPayment(obj.autoPay);
    }
    else {
        companyName.innerText = "(주)테스트컴퍼니";
        bizNum.innerText = "748-45-007911";
        userName.innerText = "김대표";
        // autopay = false;
        // productName.innerText = "스탠다드";
        // setPayment(obj.autoPay);
    }
}

function setProductCode (code) {
    userData.productCode = code;
}

function getProductCode () {
    return userData.productCode;
}

// 상품선택 리스트 세팅
function setProductTable (objArray) {
    // 상품 변경모달내부 세팅
    let productList = document.querySelector(".product_list");
    productList.innerHTML = "";

    objArray.forEach((item, index) => {
        let productItem = document.createElement("tr");
        let checked = false;
        if (userData.productCode === item.productCode) {
            checked = true
        }

        productItem.classList.add("product_item");
        if (item.productAutopay) {
            productItem.classList.add("autopay")
        }
        productItem.innerHTML = `
            <td><input type="radio" name="product_select" id="${item.productCode}" data-index="${index}" ${checked ? "checked" : ""} /></td>
            <td class='product_name'> ${item.productName} </td>
            <td class='product_price'> ${item.productPrice.toLocaleString()} 원</td>
            <td class='product_count'> ${item.productCount.toLocaleString()} 건</td>
            <td class='product_feePerCase'> ${item.productFeePerCase.toLocaleString()} 원</td>
            <td class='product_ExcessFeePerCase'>${item.productExcessFeePerCase.toLocaleString()} 원</td>
        `
        productList.append(productItem);
    });
}

function selectProduct () {
    popupClose();
    let index = document.querySelector("input[name=product_select]:checked").dataset.index
    setProductCode(productDatalist[index].productCode)
    setPayment()
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
