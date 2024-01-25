// document.addEventListener("DOMContentLoaded", function () {
//     var today = new Date();
//
//     var year = today.getFullYear(); // 년도
//     var month = ('0' + (today.getMonth() + 1)).slice(-2); // 월
//     var date = ('0' + today.getDate()).slice(-2); // 날짜
//
//     var formattedDate = year + '-' + month + '-' + date;
//     document.querySelector("#startDate").value = formattedDate;
//     document.querySelector("#endDate").value = formattedDate;
//
//     const select = document.getElementById('productTypeSelect');
//     select.addEventListener('change', function () {
//         updateDataByProduct();
//     });
//
//     const selectStartDate = document.getElementById('startDate');
//     selectStartDate.addEventListener('change', function () {
//         updateDataByProduct();
//     })
// })



var profileSpecificUrl = 'http://127.0.0.1:9000';
var profileSpecificPaymentUrl = 'http://127.0.0.1:9000';

var excessAmount = 0;

const searchParams = new URLSearchParams(window.location.search);
const agencyId = searchParams.get('agencyId');
const siteId = searchParams.get('siteId');
const startDate = searchParams.get('startDate');
const rateSel = searchParams.get('rateSel');
const openType = searchParams.get('openType');

let clientStartDate = "";

if (searchParams.get('startDate') != null) {
    clientStartDate = searchParams.get('startDate');
}

var companyName, bizNumber, ceoName;


$.ajax({
    type       : 'POST',
    url        : "/agency/payment/getPaymentInfo",
    contentType: "application/json",
    dataType   : "json",
    data       : JSON.stringify({agencyId: agencyId, siteId: siteId, rateSel: rateSel, startDate: startDate}),
    success    : function (response) {
        if (response.startDate != null) {
            // document.querySelector("#startDate").value = response.startDate;
        }
        if (response.profileUrl != null) {
            profileSpecificUrl = response.profileUrl;
        }
        if (response.profilePaymentUrl != null) {
            profileSpecificPaymentUrl = response.profilePaymentUrl;
        }

        if (response.clientInfo != null) {
            [companyName, bizNumber, ceoName]  = response.clientInfo.split(",");
            document.querySelector("#info_company_name").innerText = companyName;
            document.querySelector("#info_biz_num").innerText = bizNumber;
            document.querySelector("#info_userName").innerText = ceoName;
        }

        if (response.excessAmount != null) {
            excessAmount = response.excessAmount;
        }

        if (response.listSel) {
            populateSelect(response.listSel);
            response.listSel.forEach(e => {
                productTypeList.push({
                    type      : e.type,
                    name      : e.name,
                    price     : e.price,
                    basicOffer: e.basicOffer,
                    month     : e.month
                });
                objectArray.push({
                    type      : e.type,
                    month     : e.month,
                    productName      : e.name,
                    productPrice     : e.price,
                    productCount       : e.basicOffer,
                    productFeePerCase     : e.feePerCase,
                    productExcessFeePerCase     : e.excessFeePerCase,
                })
            })
            if (response.rateSel != null) {
                // document.querySelector("#productTypeSelect").value = response.rateSel;
                // updateDataByProduct();
            }
        } else {
            console.error('listSel is missing in the response');
            alert(JSON.stringify(response));
        }
    },
    error      : function (xhr) {
        // $("#paymentInfo").find("#paymentInfo_result").val(xhr.responseText);
        console.log(xhr.responseText);
        window.history.back();
    }
})
var objectArray = [];
var productTypeList = [];
// console.log(productTypeList);



function populateSelect(productTypes) {
    const select = document.getElementById('productTypeSelect'); // select 요소를 선택합니다.

    productTypes.forEach((product) => {
        let option = document.createElement('option'); // 새 option 요소를 생성합니다.
        option.textContent = `${product.name} - ${parseInt(product.price).toLocaleString()}원`; // 표시될 텍스트를 설정합니다.
        option.value = product.type; // option의 value를 설정합니다.

        // 필요하다면 추가 데이터를 속성으로 저장할 수 있습니다.
        option.setAttribute('data-basic-offer', product.basicOffer);
        option.setAttribute('data-month', product.month);
        option.setAttribute('data-price', product.price);
        option.setAttribute('data-name', product.name);
        option.setAttribute('data-type', product.type);

        // select.appendChild(option); // 생성된 option을 select 요소에 추가합니다.
    });
}

function updateDisplayValue(displayElementId, valueElementId, value) {
    document.getElementById(displayElementId).innerText = value.toLocaleString();
    document.getElementById(valueElementId).innerText = value;
}

// function updateDataByProduct() {
//     const select = document.getElementById('productTypeSelect');
//     const selectedOption = select.selectedIndex === -1 ? select.options[0] : select.options[select.selectedIndex];
//     selectedOption.selected = "selected";
//
//     const dataType = selectedOption.getAttribute('data-type') === "" ? "" : selectedOption.getAttribute('data-type');
//     const dataMonth = selectedOption.getAttribute('data-month') == null ? "" : selectedOption.getAttribute('data-month');
//
//     let offer, price, baseOffer, basePrice;
//
//     const startDate = new Date(document.getElementById('startDate').value);
//
//     let endDate = new Date(startDate);
//     const lastDate = new Date(endDate.getFullYear(), endDate.getMonth() + 1, 0).getDate();
//
//     const durations = lastDate - startDate.getDate() + 1;
//
//     if (dataType == "") {
//         baseOffer = 0
//         basePrice = 0
//     } else if (dataType.includes('lite')) {
//         baseOffer = parseInt(productTypeList.find(e => e.type === "lite_1m_200").basicOffer);
//         basePrice = parseInt(productTypeList.find(e => e.type === "lite_1m_200").price);
//     } else if (dataType.includes('basic')) {
//         baseOffer = parseInt(productTypeList.find(e => e.type === "basic_1m_1000").basicOffer);
//         basePrice = parseInt(productTypeList.find(e => e.type === "basic_1m_1000").price);
//     } else if (dataType.includes('standard')) {
//         baseOffer = parseInt(productTypeList.find(e => e.type === "standard_1m_2000").basicOffer);
//         basePrice = parseInt(productTypeList.find(e => e.type === "standard_1m_2000").price);
//     } else if (dataType.includes('premium')) {
//         baseOffer = parseInt(productTypeList.find(e => e.type === "premium_1m_3000").basicOffer);
//         basePrice = parseInt(productTypeList.find(e => e.type === "premium_1m_3000").price);
//     }
//
//     offer = (baseOffer * (dataMonth - 1)) + (baseOffer * durations / lastDate);
//     price = ((basePrice * durations / lastDate) + (basePrice * (dataMonth - 1))) * 1.1 + excessAmount;
//
//     if (dataMonth === '1') {
//         if (durations <= 15) {
//             endDate = new Date(endDate.getFullYear(), endDate.getMonth() + Number(dataMonth) + 1, 0);
//             offer = (baseOffer) + (baseOffer * durations / lastDate);
//             price = ((basePrice * durations / lastDate) + basePrice) * 1.1 + excessAmount;
//         } else {
//             offer = baseOffer * durations / lastDate;
//             price = (basePrice * durations / lastDate) * 1.1 + excessAmount;
//         }
//     } else {
//         if (dataMonth === '12') {
//             endDate.setFullYear(endDate.getFullYear() + 1);
//             endDate.setMonth(endDate.getMonth() - 1);
//         } else if (dataMonth === '6') {
//             endDate.setMonth(endDate.getMonth() + 5);
//         }
//     }
//
//     endDate = new Date(endDate.getFullYear(), endDate.getMonth() + 1, 0);
//
//     var year = endDate.getFullYear();
//     var month = ('0' + (endDate.getMonth() + 1)).slice(-2);
//     var date = ('0' + endDate.getDate()).slice(-2);
//
//     var formattedEndDate = year + '-' + month + '-' + date;
//     document.getElementById('endDate').value = formattedEndDate;
//     document.getElementById('offer').value = Math.floor(offer);
//     document.getElementById('price').value = Math.floor(price);
//
//     let offerValue = Math.floor(offer); // 실제 값
//     let priceValue = Math.floor(price); // 실제 값
//
//     updateDisplayValue('offerDisplay', 'offer', offerValue);
//     updateDisplayValue('priceDisplay', 'price', priceValue);
// }

function updateDataByProduct() {
    const selectedOption  = window.selectedProductType;
    console.log(selectedOption);
    const dataType = selectedOption[0].type === "" ? "" : selectedOption[0].type;
    const dataMonth = selectedOption[0].month == null ? "" : selectedOption[0].month;

    console.log("dataType : "  + dataType)
    console.log("dataMonth : " + dataMonth)

    let offer, price, baseOffer, basePrice;

    const startDate = new Date(document.getElementById('datepicker').value);

    let endDate = new Date(startDate);
    const lastDate = new Date(endDate.getFullYear(), endDate.getMonth() + 1, 0).getDate();

    const durations = lastDate - startDate.getDate() + 1;

    if (dataType == "") {
        baseOffer = 0
        basePrice = 0
    } else if (dataType.includes('lite')) {
        baseOffer = parseInt(productTypeList.find(e => e.type === "lite_1m_200").basicOffer);
        basePrice = parseInt(productTypeList.find(e => e.type === "lite_1m_200").price);
    } else if (dataType.includes('basic')) {
        baseOffer = parseInt(productTypeList.find(e => e.type === "basic_1m_1000").basicOffer);
        basePrice = parseInt(productTypeList.find(e => e.type === "basic_1m_1000").price);
    } else if (dataType.includes('standard')) {
        baseOffer = parseInt(productTypeList.find(e => e.type === "standard_1m_2000").basicOffer);
        basePrice = parseInt(productTypeList.find(e => e.type === "standard_1m_2000").price);
    } else if (dataType.includes('premium')) {
        baseOffer = parseInt(productTypeList.find(e => e.type === "premium_1m_3000").basicOffer);
        basePrice = parseInt(productTypeList.find(e => e.type === "premium_1m_3000").price);
    }

    offer = (baseOffer * (dataMonth - 1)) + (baseOffer * durations / lastDate);
    price = ((basePrice * durations / lastDate) + (basePrice * (dataMonth - 1))) * 1.1 + excessAmount;

    if (dataMonth === '1') {
        if (durations <= 15) {
            endDate = new Date(endDate.getFullYear(), endDate.getMonth() + Number(dataMonth) + 1, 0);
            offer = (baseOffer) + (baseOffer * durations / lastDate);
            price = ((basePrice * durations / lastDate) + basePrice) * 1.1 + excessAmount;
        } else {
            offer = baseOffer * durations / lastDate;
            price = (basePrice * durations / lastDate) * 1.1 + excessAmount;
        }
    } else {
        if (dataMonth === '12') {
            endDate.setFullYear(endDate.getFullYear() + 1);
            endDate.setMonth(endDate.getMonth() - 1);
        } else if (dataMonth === '6') {
            endDate.setMonth(endDate.getMonth() + 5);
        }
    }

    endDate = new Date(endDate.getFullYear(), endDate.getMonth() + 1, 0);

    var year = endDate.getFullYear();
    var month = ('0' + (endDate.getMonth() + 1)).slice(-2);
    var date = ('0' + endDate.getDate()).slice(-2);

    var formattedEndDate = year + '-' + month + '-' + date;
    // document.getElementById('endDate').value = formattedEndDate;
    // document.getElementById('offer').value = Math.floor(offer);
    // document.getElementById('price').value = Math.floor(price);

    let offerValue = Math.floor(offer); // 실제 값
    let priceValue = Math.floor(price); // 실제 값


    console.log("offerValue : " + offerValue)
    console.log("priceValue : " + priceValue)
    updateDisplayValue('offerDisplay', 'offer', offerValue);
    updateDisplayValue('priceDisplay', 'price', priceValue);
}

function pay(type) {
    var curr_date = new Date();
    var year = curr_date.getFullYear().toString();
    var month = ("0" + (curr_date.getMonth() + 1)).slice(-2).toString();
    var day = ("0" + (curr_date.getDate())).slice(-2).toString();
    var hours = ("0" + curr_date.getHours()).slice(-2).toString();
    var mins = ("0" + curr_date.getMinutes()).slice(-2).toString();
    var secs = ("0" + curr_date.getSeconds()).slice(-2).toString();
    var random4 = ("000" + Math.random() * 10000).slice(-4).toString();

    // var custIp    = "127.0.0.1" // 세팅 필요
    var trdDt = year + month + day;
    var trdTm = hours + mins + secs;
    var mchtTrdNo = "PAYMENT" + year + month + day + hours + mins + secs + random4;//주문번호 세팅


    var method = type;

    var mchtId;
    if (type === "card") {
        mchtId = "nxca_jt_il"
    } else if (type === "card_auto") {
        mchtId = "nxca_jt_gu"
        method = "card";
    }
    // vbank
    else {
        mchtId = "nx_mid_il"
    }

    const productTypeSelect = document.getElementById('productTypeSelect');
    const selectedOption = productTypeSelect.options[productTypeSelect.selectedIndex];
    const productName = selectedOption.getAttribute('data-name');
    const productValue = productTypeSelect.value;

    if (productValue === null || productValue === '') {
        alert('상품 타입을 선택해주세요.');
        return;
    }

    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    var trdAmt = document.getElementById("price").value;
    var plainMchtCustNm = ""
    var plainEmail = ""
    var plainMchtCustId = "";
    var mchtName = "드림시큐리티"
    var mchtEName = "dreamsecurity"
    var pmtPrdtNm = productName + " (" + startDate + " - " + endDate + ")";

    const mchtParam = "agencyId=" + agencyId + "&siteId=" + siteId + "&clientStartDate=" + clientStartDate + "&startDate=" + startDate + "&endDate=" + endDate + "&rateSel=" + selectedOption.value + "&offer=" + document.getElementById("offer").value + "&companyName=" + companyName + "&bizNumber=" + bizNumber + "&ceoName=" + ceoName;

    var data = {
        mchtId              : mchtId,
        method              : method,
        mchtTrdNo           : mchtTrdNo,
        trdDt               : trdDt,
        trdTm               : trdTm,
        mchtParam           : mchtParam,
        plainTrdAmt         : trdAmt,
        plainMchtCustNm     : plainMchtCustNm,
        plainCphoneNo       : "",
        plainEmail          : plainEmail,
        plainMchtCustId     : plainMchtCustId,
        plainTaxAmt         : "",
        plainVatAmt         : "",
        plainTaxFreeAmt     : "",
        plainSvcAmt         : "",
        plainClipCustNm     : "",
        plainClipCustCi     : "",
        plainClipCustPhoneNo: ""
    }


    let uiType = "";

    switch (openType) {
        case "redirect" : {
            uiType = "self";
            break;
        }
        case "popup" : {
            uiType = "popup";
            break;
        }
        default : {
            uiType = "popup";
            break;
        }
    }

    $.ajax({
        type       : "POST",
        url        : profileSpecificPaymentUrl + "/agency/payment/setPaymentSiteInfo",
        contentType: "application/json",
        dataType   : "json",
        data       : JSON.stringify(data),
        success    : function (response) {
            if (response.resultCode === "2000") {
                console.log(JSON.stringify(response));
                //가맹점 -> 세틀뱅크로 결제 요청
                SETTLE_PG.pay({
                    env       : "https://tbnpg.settlebank.co.kr",   //결제서버 URL
                    mchtId    : mchtId,
                    method    : method,
                    trdDt     : trdDt,
                    trdTm     : trdTm,
                    mchtTrdNo : mchtTrdNo,
                    mchtName  : mchtName,
                    mchtEName : mchtEName,
                    mchtParam : mchtParam,
                    pmtPrdtNm : pmtPrdtNm,
                    trdAmt    : response.encParams.trdAmt,
                    mchtCustNm: response.encParams.mchtCustNm,
                    notiUrl   : profileSpecificUrl + "/agency/payment/api/result/noti",
                    nextUrl   : profileSpecificUrl + "/agency/payment/api/result/next",
                    cancUrl   : profileSpecificUrl + "/agency/payment/api/result/cancel",
                    pktHash   : response.hashCipher,
                    ui        : {
                        // type  : "popup",   //popup, iframe, self, blank
                        type  : uiType,   //popup, iframe, self, blank
                        width : "430",   //popup창의 너비
                        height: "660"   //popup창의 높이
                    }
                }, function (response) {
                    //iframe인 경우 전달된 결제 완료 후 응답 파라미터 처리

                    console.log(response);
                });
            } else {
                alert(JSON.stringify(response))
                console.log(response);
            }
        }, error   : function () {
            alert("에러 발생");
        },
    })
}


function popupOpen(popNum) {
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
            setProductTable(objectArray);

            document.querySelectorAll("input[name='product_select']").forEach(radio => {
                radio.addEventListener('change', function() {
                    console.log(this.checked)
                    if(this.checked) { // radio button이 선택된 상태라면
                        console.log(this.parentElement.parentElement.querySelector('.product_name').innerText)
                        window.selectedProductName = this.parentElement.parentElement.querySelector('.product_name').innerText;
                        window.selectedProductType = []
                        window.selectedProductType.push({
                            type      : this.parentElement.parentElement.querySelector('.type').innerText,
                            productName      : this.parentElement.parentElement.querySelector('.product_name').innerText,
                            productPrice     : this.parentElement.parentElement.querySelector('.product_price').innerText,
                            productCount       : this.parentElement.parentElement.querySelector('.product_count').innerText,
                            productFeePerCase     : this.parentElement.parentElement.querySelector('.product_feePerCase').innerText,
                            productExcessFeePerCase     : this.parentElement.parentElement.querySelector('.product_ExcessFeePerCase').innerText,
                        })
                    }
                });
            });
            break;
        }
    }
}

function popupClose(popNum) {
    $("body").removeClass("stop-scrolling");
    $(".popup_overlay").removeClass("active");
    $(".popup_wrap").removeClass("active");
    $(".modal_wrap").removeClass("active");
    switch (popNum) {
        case 1: {
            $("#datepicker").focus();
           break;
        }
        case 2 :{
            if (window.selectedProductName) { //만약 상품이 선택되었다면
                document.querySelector('#info-item-text-product-name').innerText = window.selectedProductName;
                updateDataByProduct();
            }
        }
    }
}


