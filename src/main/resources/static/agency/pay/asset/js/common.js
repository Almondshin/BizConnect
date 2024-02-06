let requestData = {
    info: {
        companyName: "",
        bizName: "",
        name: "",
        bizNum: "",
        number: "",
        email: "",
        serviceUrl: "",
        address1: "",
        address2: "",
    },
    manager: {
        name: "",
        phone: "",
        tel: "",
        email: "",
    },
    product: {},
};
let focusItem;
// 1,2,3 페이지
// 신청하기 버튼 클릭시 값을 체크하고 정보 확인페이지로 이동
function requestServiceBtn() {
    const inputList = document.querySelectorAll(".info-item-input");
    const agreeList = document.querySelectorAll("input[name=agree]");
    const productList = document.querySelectorAll("input[name=product_select]");

    let shouldBreakcheckbox = false;
    let shouldBreakproduct = false;
    let isChecked = false;

    for (let i = 0; i < inputList.length; i++) {
        if (!checkPattern(inputList[i])) {
            focusItem = inputList[i];
            popupOpen(3);
            setPopup("requiredInfo");
            shouldBreakcheckbox = true;
            break;
        }
    }
    if (!shouldBreakcheckbox) {
        for (let i = 0; i < agreeList.length; i++) {
            if (!agreeList[i].checked) {
                focusItem = agreeList[i];
                popupOpen(3);
                setPopup("agree");
                shouldBreakproduct = true;
                break;
            }
        }
    }
    if (!shouldBreakproduct && !shouldBreakcheckbox) {
        console.log(productList);
        if (!getSelectedProduct()) {
            focusItem = productList[0];
            popupOpen(3);
            setPopup("product");
            isChecked = true;
        } else {
            requestData.product = getSelectedProduct();
        }
    }
    if (!shouldBreakproduct && !shouldBreakcheckbox && !isChecked) {
        moveInfoCheckpage(true);
    }
}
function checkPattern(elemnt) {
    let boolean = false;
    switch (elemnt.id) {
        case "manager_email": {
            let pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            {
                pattern.test(elemnt.value) ? (boolean = true) : (boolean = false);
            }
            break;
        }
        case "manager_tel": {
            let pattern = /^\+?\d{1,4}[-.\s]?\(?\d{1,4}\)?[-.\s]?\d{1,9}[-.\s]?\d{1,9}$/;
            {
                pattern.test(elemnt.value) ? (boolean = true) : (boolean = false);
            }
            break;
        }
        case "manager_phone": {
            let pattern = /^\+?\d{1,4}[-.\s]?\(?\d{1,4}\)?[-.\s]?\d{1,9}[-.\s]?\d{1,9}$/;
            {
                pattern.test(elemnt.value) ? (boolean = true) : (boolean = false);
            }
            break;
        }
        case "input_service_url": {
            let pattern = /[^\s/$.?#].[^\s]*$/;
            {
                pattern.test(elemnt.value) ? (boolean = true) : (boolean = false);
            }
            break;
        }
        case "info_email": {
            let pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            {
                pattern.test(elemnt.value) || elemnt.value.length == 0 ? (boolean = true) : (boolean = false);
            }
            break;
        }
        case "info_number": {
            let pattern = /^\+?\d{1,4}[-.\s]?\(?\d{1,4}\)?[-.\s]?\d{1,9}[-.\s]?\d{1,9}$/;
            {
                pattern.test(elemnt.value) ? (boolean = true) : (boolean = false);
            }
            break;
        }
        case "info_bizNum": {
            {
                (elemnt.value + "").length === 10 ? (boolean = true) : (boolean = false);
            }
            break;
        }
        default: {
            {
                elemnt.value > 0 || elemnt.value.length > 0 ? (boolean = true) : (boolean = false);
            }
        }
    }
    return boolean;
}
function moveInfoCheckpage(boolean) {
    let formPage = document.querySelectorAll(".info-form-page");
    let checkPage = document.querySelectorAll(".info-check-page");
    if (boolean) {
        for (let i = 0; i < formPage.length; i++) {
            formPage[i].classList.remove("active");
        }
        for (let i = 0; i < checkPage.length; i++) {
            checkPage[i].classList.add("active");
        }
        setRequestData();
    } else {
        for (let i = 0; i < formPage.length; i++) {
            formPage[i].classList.add("active");
        }
        for (let i = 0; i < checkPage.length; i++) {
            checkPage[i].classList.remove("active");
        }
    }
}
function setRequestData() {
    requestData = {
        info: {
            companyName: document.querySelector("#info_company_name").value,
            bizName: document.querySelector("#info_bizName").value,
            name: document.querySelector("#info_name").value,
            bizNum: document.querySelector("#info_bizNum").value,
            number: document.querySelector("#info_number").value,
            email: document.querySelector("#info_email").value,
            serviceUrl: document.querySelector("#info_serviceUrl").value,
            address1: document.querySelector("#info_address1").value,
            address2: document.querySelector("#info_address2").value,
        },
        manager: {
            name: document.querySelector("#manager_name").value,
            phone: document.querySelector("#manager_phone").value,
            tel: document.querySelector("#manager_tel").value,
            email: document.querySelector("#manager_email").value,
        },
        product: {
            name: getProductName(requestData.product.productCode),
            autopay: `${requestData.product.productAutopay ? "사용" : "사용하지않음"}`,
        },
    };
    setInfoCheckpage(requestData);
}
function setInfoCheckpage(obj) {
    console.log(obj);
    document.querySelector("#confirm_info_company_name").innerText = `${obj.info.companyName}`;
    document.querySelector("#confirm_info_biz_num").innerText = `${obj.info.bizNum}`;
    document.querySelector("#confirm_info_name").innerText = `${obj.info.name}`;
    document.querySelector("#confirm_info_email").innerText = `${obj.info.email ? obj.info.email : "미 입력"}`;
    document.querySelector("#confirm_info_number").innerText = `${obj.info.number}`;
    document.querySelector("#confirm_info_address").innerText = `${obj.info.address1} ${obj.info.address2}`;

    document.querySelector("#confirm_manager_name").innerText = `${obj.manager.name}`;
    document.querySelector("#confirm_manager_phone").innerText = `${obj.manager.phone}`;
    document.querySelector("#confirm_manager_tel").innerText = `${obj.manager.tel}`;
    document.querySelector("#confirm_manager_email").innerText = `${obj.manager.email ? obj.manager.email : "미 입력"}`;

    document.querySelector("#confirm_product_name").innerText = `${obj.product.name}`;
    document.querySelector("#confirm_product_autopay").innerText = `${getProductAutopay(obj.product.autopay) ? "사용" : "사용하지않음"}`;
}
// 서비스 신청페이지의 input에 이벤트 부여
function requestServiceInputInit() {
    const inputList = document.querySelectorAll(".info-item-input");
    inputList.forEach((itm) => {
        itm.addEventListener("focusin", function () {
            // this.classList.remove("invalid");
            document.querySelector(`label[for=${itm.id}]`).classList.remove("invalid");
        });
    });
    inputList.forEach((itm) => {
        itm.addEventListener("focusout", function () {
            if (checkPattern(itm)) {
                this.classList.remove("invalid");
                document.querySelector(`label[for=${itm.id}]`).classList.remove("invalid");
            } else {
                this.classList.add("invalid");
                document.querySelector(`label[for=${itm.id}]`).classList.add("invalid");
            }
        });
    });
}

// 4, 4.1 페이지
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
    let paymentRadioWrapper = document.querySelector(".info-itme-howtopay");
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
    } else {
        setPopup("date");
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
