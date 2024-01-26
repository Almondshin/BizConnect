var profileSpecificUrl = 'http://127.0.0.1:9000';
var profileSpecificPaymentUrl = 'http://127.0.0.1:9000';

var openType = "";

var init = function () {
    process.data.agencyId = utils.getParam("agencyId");
    process.data.siteId = utils.getParam("siteId");
    process.data.startDate = utils.getParam("startDate");
    process.data.rateSel = utils.getParam("rateSel");

    openType = utils.getParam("openType");

    process.getPaymentInfo();
    document.querySelector("#payment").setAttribute("onclick", "process.setPaymentSiteInfo()");

}

var process = {
    getPaymentInfo: function () {
        $.ajax({
            type       : 'POST',
            url        : "/agency/payment/getPaymentInfo",
            contentType: "application/json",
            dataType   : "json",
            data       : JSON.stringify({
                agencyId: process.data.agencyId,
                siteId: process.data.siteId,
                rateSel: process.data.rateSel,
                startDate: process.data.startDate
            }),
            success    : function (response) {
                if (response.startDate != null) {
                    document.querySelector("#datepicker").value = response.startDate;
                }
                if (response.profileUrl != null) {
                    profileSpecificUrl = response.profileUrl;
                }
                if (response.profilePaymentUrl != null) {
                    profileSpecificPaymentUrl = response.profilePaymentUrl;
                }

                if (response.excessAmount != null) {
                    excessAmount = response.excessAmount;
                }

                if (response.listSel) {
                    Array.prototype.forEach.call(response.listSel, function (el, index){
                        page4_1_tableData[index] = {
                            type      : el.type,
                            month     : el.month,
                            productName      : el.name,
                            productPrice     : el.price,
                            productCount       : el.basicOffer,
                            productFeePerCase     : el.feePerCase,
                            productExcessFeePerCase     : el.excessFeePerCase,
                        }
                    })

                    setProductTable(page4_1_tableData);
                    document.querySelector(".info-item .btn_purple").setAttribute("onclick", "popupOpen(3)");

                    document.querySelector(".modal_footer .btn_purple").setAttribute("onclick", "process.setProduct()");

                    if (response.clientInfo != null) {
                        [companyName, bizNumber, ceoName]  = response.clientInfo.split(",");
                        setInformation_4({
                            companyName: companyName,
                            bizNum: bizNumber,
                            userName: ceoName
                        })
                    }

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
    }
    , setPaymentSiteInfo: function () {
        payProgress();
        var request = new XMLHttpRequest();
        request.open("POST", BASE_URL + "/agency/payment/setPaymentSiteInfo", false);
        request.setRequestHeader("Content-type", "application/json");
        request.onreadystatechange = function () {
            process.HFPayment(JSON.parse(this.response))
        }

        process.data.method = document.querySelector("input[name=howtopay]:checked").value;

        process.data.startDate = document.querySelector("#datepicker").value;
        process.data.rateSel = process.selectedProduct.type;
        console.log("request Data : " + JSON.stringify(process.data))
        request.send(JSON.stringify(process.data));
    }
    , select: function () {
        var price = parseInt(process.selectedProduct.productPrice)
        var duration = parseInt(process.selectedProduct.month);
        var offer = parseInt(process.selectedProduct.productCount);

        var selectStartDate = document.querySelector("#datepicker").value;
        selectStartDate = new Date(selectStartDate);
        var currentDate = new Date(selectStartDate.getTime());
        var currentLastDate = new Date(selectStartDate.getTime());
        currentLastDate = new Date(currentLastDate.setMonth(currentLastDate.getMonth() + 1));
        currentLastDate = new Date(currentLastDate.setDate(0));
        var selectEndDate = new Date(selectStartDate.getTime());


        var remainDate = currentLastDate.getDate() - currentDate.getDate() + 1
        if (duration === 1) {
            if (remainDate <= 14) {
                selectEndDate = new Date(selectEndDate.setMonth(selectEndDate.getMonth() + 2));
                selectEndDate = new Date(selectEndDate.setDate(0));
            }
            else {
                selectEndDate = new Date(selectEndDate.setMonth(selectEndDate.getMonth() + 1));
                selectEndDate = new Date(selectEndDate.setDate(0));
            }
        }
        else {
            selectEndDate = new Date(selectEndDate.setMonth(selectEndDate.getMonth() + duration));
            selectEndDate = new Date(selectEndDate.setDate(0));
        }

        selectEndDate = selectEndDate.toISOString().slice(0, 10);
        selectStartDate = selectStartDate.toISOString().slice(0, 10);
        process.data.endDate = selectEndDate;
        // document.querySelector("#endDate").value = last.getFullYear() + "-" + ((last.getMonth() + 1) < 10 ? "0" + (last.getMonth() + 1) : (last.getMonth() + 1)) + "-" + last.getDate();

        if (duration === 1) {
            if (remainDate <= 14) {
                duration = (remainDate / currentLastDate.getDate()) + 1;
            }
            else {
                duration = (remainDate / currentLastDate.getDate());
            }
        }
        else {
            duration = ((remainDate / currentLastDate.getDate()) + (duration - 1)) / duration;
        }
        process.data.salesPrice = Math.floor(price * duration * 1.1).toString()
        process.data.offer = Math.floor(offer * duration).toString()
        this.selectedProduct.startDate = selectStartDate;
        this.selectedProduct.endDate = selectEndDate;

        document.getElementById('datepicker_end').value = this.selectedProduct.endDate;

        const calculatedPrice = Math.floor(price * duration * 1.1);
        const formattedPrice = calculatedPrice.toLocaleString();
        const offerPrice = Math.floor(offer * duration);
        document.querySelector("#selectedProductPrice").innerText = formattedPrice + " 원 " + "(" + offerPrice.toLocaleString() + " 건)";

        console.log(`선택일 : ${currentDate.toISOString().slice(0, 10)} 선택월 말일 : ${currentLastDate.toISOString().slice(0, 10)} 종료일 : ${selectEndDate}
                    \n가격 : ${Math.floor(price * duration * 1.1)} 제공건수 : ${Math.floor(offer * duration)}`)
    }
    , HFPayment: function (response) {
        if (response.resultCode === "2000") {
            var mchtName = "드림시큐리티"
            var mchtEName = "Dreamsecurity"

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
            console.log(response.mchtId)

            SETTLE_PG.pay({
                env       : "https://tbnpg.settlebank.co.kr",   //결제서버 URL
                // env       : "https://npg.settlebank.co.kr",   //결제서버 URL
                mchtName  : mchtName,
                mchtEName : mchtEName,
                pmtPrdtNm : this.selectedProduct.productName + "(" + this.selectedProduct.startDate +"-"+ this.selectedProduct.endDate + ")",
                mchtId    : response.mchtId,
                method    : response.method,
                trdDt     : response.trdDt,
                trdTm     : response.trdTm,
                mchtTrdNo : response.mchtTrdNo,
                trdAmt    : response.encParams.trdAmt,
                pktHash   : response.hashCipher,
                notiUrl   : profileSpecificUrl + "/agency/payment/api/result/noti2",
                nextUrl   : profileSpecificUrl + "/agency/payment/api/result/next",
                cancUrl   : profileSpecificUrl + "/agency/payment/api/result/cancel",
                ui        : {
                    // type  : "popup",   //popup, iframe, self, blank
                    type  : uiType,   //popup, iframe, self, blank
                    width : "430",   //popup창의 너비
                    height: "660"   //popup창의 높이
                }
            }, function (rsp) {
                //iframe인 경우 전달된 결제 완료 후 응답 파라미터 처리
                console.log(rsp);
            });
        } else{
            alert(JSON.stringify(response))
            console.log(response);
        }
    }

    , setProduct: function () {
        let index = document.querySelector("input[name=product_select]:checked").dataset.productIndex
        this.selectedProduct = page4_1_tableData[index];
        document.querySelector("#selectedProductName").innerText = this.selectedProduct.productName;
        process.select();
        popupClose();
    }
    , selectedProduct: {}
    , data: {
        agencyId: "",
        siteId: "",
        startDate: "",
        endDate: "",
        rateSel: "",
        salesPrice: "",
        method: "",
    }
}


var utils = {
    getParam: function (name) {
        var search = location.search.substring(location.search.indexOf("?") + 1);
        var value;

        console.log("search : " + search)
        search = search.split("&");

        for (var i = 0; i < search.length; i++) {
            var temp = search[i].split("=");
            if (temp[0] === name) {
                value = temp[1];
            }
        }
        return value !== undefined ? decodeURIComponent(value) : value;
    }
}


