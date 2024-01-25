const searchParams = new URLSearchParams(window.location.search);
const agencyId = searchParams.get('agencyId');
const siteId = searchParams.get('siteId');
const startDate = searchParams.get('startDate');
const rateSel = searchParams.get('rateSel');
const openType = searchParams.get('openType');
var objectArray = [];
var productTypeList = [];

var init = function () {
    // process.data.agencyId = utils.getParam("agencyId");
    // process.data.siteId = utils.getParam("siteId");
    // openType = utils.getParam("openType");




    process.getPaymentInfo();
    // document.querySelector("#payment").setAttribute("onclick", "process.setPaymentSiteInfo()");

}

var process = {
    getPaymentInfo: function () {
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

                if (response.excessAmount != null) {
                    excessAmount = response.excessAmount;
                }

                if (response.listSel) {
                    // populateSelect(response.listSel);
                    // response.listSel.forEach(e => {
                    //     // productTypeList.push({
                    //     //     type      : e.type,
                    //     //     name      : e.name,
                    //     //     price     : e.price,
                    //     //     basicOffer: e.basicOffer,
                    //     //     month     : e.month
                    //     // });
                    //     objectArray.push({
                    //         type      : e.type,
                    //         month     : e.month,
                    //         productName      : e.name,
                    //         productPrice     : e.price,
                    //         productCount       : e.basicOffer,
                    //         productFeePerCase     : e.feePerCase,
                    //         productExcessFeePerCase     : e.excessFeePerCase,
                    //     })
                    // })

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
        console.log(JSON.stringify(process.data))
        request.send(JSON.stringify(process.data));
    }
    , select: function () {

        var price = parseInt(process.selectedProduct.productPrice)
        var duration = parseInt(process.selectedProduct.duration);
        var offer = parseInt(process.selectedProduct.productCount);

        var startDate = document.querySelector("#datepicker").value;

        startDate = startDate.split("-");

        var curr = new Date(startDate[0], parseInt(startDate[1]) - 1, startDate[2]);

        var currLast = new Date(curr.getFullYear(), curr.getMonth() + 1, 0);

        var last = new Date(curr.getFullYear(), (curr.getMonth() + parseInt(duration)), 0);

        if (duration === 1) {
            if (currLast.getDate() - curr.getDate() + 1 <= 14) {
                last = new Date(last.getFullYear(), last.getMonth() + 2, 0);

            }
            else {
                last = new Date(last.getFullYear(), last.getMonth() + 1, 0);
            }
        }
        else {
            last = new Date(last.getFullYear(), last.getMonth() + 1, 0);

        }
        console.log(last.getFullYear() + "-" + ((last.getMonth() + 1) < 10 ? "0" + (last.getMonth() + 1) : (last.getMonth() + 1)) + "-" + last.getDate());
        process.data.endDate = (last.getFullYear() + "-" + ((last.getMonth() + 1) < 10 ? "0" + (last.getMonth() + 1) : (last.getMonth() + 1)) + "-" + last.getDate()).toString();
        // document.querySelector("#endDate").value = last.getFullYear() + "-" + ((last.getMonth() + 1) < 10 ? "0" + (last.getMonth() + 1) : (last.getMonth() + 1)) + "-" + last.getDate();

        if (duration === 1) {
            if (currLast.getDate() - curr.getDate() + 1 <= 14) {
                duration = ((currLast.getDate() - curr.getDate() + 1) / currLast.getDate()) + 1;
            }
            else {
                duration = ((currLast.getDate() - curr.getDate() + 1) / currLast.getDate());
            }
        }
        else {
            duration = (((currLast.getDate() - curr.getDate() + 1) / currLast.getDate()) + (duration - 1)) / duration;
        }

        console.log(Math.floor(price * duration * 1.1));
        process.data.salesPrice = Math.floor(price * duration * 1.1).toString()
        console.log(Math.floor(offer * duration));
        process.data.offer = Math.floor(offer * duration).toString()
    }
    , HFPayment: function (res) {
        var mchtName = "드림시큐리티"
        var mchtEName = "Dreamsecurity"
        var type = "popup"
        if (openType === "popup") {
            type = "popup"
        }
        if (openType === "redirect") {
            type = "self"
        }

        SETTLE_PG.pay({
            mchtName: mchtName,
            mchtEName: mchtEName,
            env: "https://tbnpg.settlebank.co.kr",   //결제서버 URL
            // env: "https://npg.settlebank.co.kr",   //결제서버 URL
            mchtId: res.mchtId,
            method: res.method,
            mchtTrdNo: res.mchtTrdNo,
            trdDt: res.trdDt,
            trdTm: res.trdTm,
            pmtPrdtNm: res.pmtPrdtNm,
            trdAmt: res.trdAmt,
            mchtCustNm: res.mchtCustNm,
            notiUrl: BASE_URL + "/agency/payment/noti",
            nextUrl: BASE_URL + "/agency/payment/next",
            cancUrl: BASE_URL + "/agency/payment/cancel",
            mchtParam: res.mchtParam,
            pktHash: res.pktHash,


            ui: {
                type: type,   //popup, iframe, self, blank
                width: "430",   //popup창의 너비
                height: "660"   //popup창의 높이
            }
        }, function (rsp) {
            //iframe인 경우 전달된 결제 완료 후 응답 파라미터 처리
            console.log(rsp);
        });
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
        method: ""
    }

}


var utils = {
    getParam: function (name) {
        var search = location.search.substring(location.search.indexOf("?") + 1);
        var value;

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


