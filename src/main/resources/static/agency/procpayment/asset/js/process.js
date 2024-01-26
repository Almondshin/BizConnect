const preInputParameter = {
    agencyId: "",
    siteId: "",
    startDate: "",
    rateSel: "",
    openType: "",
}

var openType = "";

const init = function () {
    document.querySelector("#selectProductPopUpOpen").addEventListener("click", function () {
        popupOpen(2)
    })
    document.querySelector("#selectProduct").addEventListener("click", function () {
        process.setProduct();
        selectProduct()
    })
    document.querySelector("#cancelSelectProduct").addEventListener("click", function () {
        popupClose()
    })
    document.querySelector("#payment").addEventListener("click", function () {
        payProgress()
        process.setPaymentSiteInfo();
    })

    process.data.agencyId = utils.getParam("agencyId");
    process.data.siteId = utils.getParam("siteId");
    process.data.startDate = utils.getParam("startDate");
    process.data.rateSel = utils.getParam("rateSel");

    openType = utils.getParam("openType");

    for (let key in preInputParameter) {
        preInputParameter[key] = utils.getParam(key)
    }

    if (preInputParameter.rateSel !== "") {
        setProductCode(preInputParameter.rateSel);
    }

    if (preInputParameter.startDate !== "") {
        document.querySelector("#datepicker").value = preInputParameter.startDate;
    }

    process.getPaymentInfo();
}

const process = {
    data: {
        agencyId: "",
        siteId: "",
        startDate: "",
        endDate: "",
        rateSel: "",
        salesPrice: "",
        method: ""
    }
    , selectedProduct: {}
    , getPaymentInfo: function () {
        const request = new XMLHttpRequest();
        request.open("POST", profileSpecificUrl + "/agency/payment/getPaymentInfo", false);
        request.setRequestHeader("Content-type", "application/json");

        request.onreadystatechange = function () {
            let response = {
                listSel: "",
                clientInfo:"",
                profileUrl:"",
                profilePaymentUrl:"",
            }
            response = JSON.parse(this.response);

            [companyName, bizNumber, ceoName]  = response.clientInfo.split(",");
            setInformation_4({
                companyName: companyName,
                bizNum: bizNumber,
                userName: ceoName
            })

            if (response.startDate != null) {
                document.querySelector("#datepicker").value = response.startDate;
            }

            if (response.profileUrl != null) {
                profileSpecificUrl = response.profileUrl;
            }
            if (response.profilePaymentUrl != null) {
                profileSpecificPaymentUrl = response.profilePaymentUrl;
            }

            productDatalist = [];
            Array.prototype.forEach.call(response.listSel, function (el, index) {
                productDatalist[index] = {
                    productCode: el.type,
                    productName: el.name,
                    productPrice: el.price,
                    productDuration: el.month,
                    productCount: el.basicOffer,
                    productFeePerCase: el.feePerCase,
                    productExcessFeePerCase: el.excessFeePerCase,
                    productAutopay: el.type.indexOf("autopay") > -1 ? true : false,
                }
            })

            setProductTable(productDatalist)
            calcPrice = process.select;
            if (preInputParameter.rateSel !== "") {
                userData.productCode = preInputParameter.rateSel;
                process.setProduct();
                setPayment();
            }
        }
        let data = {
            agencyId: process.data.agencyId,
            siteId: process.data.siteId,
        }
        request.send(JSON.stringify(data))
    }
    , setPaymentSiteInfo: function () {
        let request = new XMLHttpRequest();
        request.open("POST", profileSpecificUrl + "/agency/payment/setPaymentSiteInfo", false);
        request.setRequestHeader("Content-type", "application/json");
        request.onreadystatechange = function () {
            process.HFPayment(JSON.parse(this.response))
        }

        process.data.method = document.querySelector("input[name=howtopay]:checked").value;
        process.data.startDate = document.querySelector("#datepicker").value;
        process.data.rateSel = process.selectedProduct.productCode;
        console.log(JSON.stringify(process.data))
        request.send(JSON.stringify(process.data));
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
                notiUrl   : profileSpecificUrl + "/agency/payment/api/result/noti",
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
    , select: function () {
        var price = parseInt(process.selectedProduct.productPrice)
        var duration = parseInt(process.selectedProduct.productDuration);
        var offer = parseInt(process.selectedProduct.productCount);

        var startDate = document.querySelector("#datepicker").value;
        startDate = new Date(startDate);
        var currentDate = new Date(startDate.getTime());
        var currentLastDate = new Date(startDate.getTime());
        currentLastDate = new Date(currentLastDate.setDate(1));
        currentLastDate = new Date(currentLastDate.setMonth(currentLastDate.getMonth() + 1));
        currentLastDate = new Date(currentLastDate.setDate(0));
        var endDate = new Date(startDate.getTime());

        var remainDate = currentLastDate.getDate() - currentDate.getDate() + 1
        if (duration === 1) {
            if (remainDate <= 14) {
                endDate = new Date(endDate.setMonth(endDate.getMonth() + 2));
                endDate = new Date(endDate.setDate(0));
            }
            else {
                endDate = new Date(endDate.setMonth(endDate.getMonth() + 1));
                endDate = new Date(endDate.setDate(0));
            }
        }
        else {
            endDate = new Date(endDate.setMonth(endDate.getMonth() + duration));
            endDate = new Date(endDate.setDate(0));
        }

        endDate = endDate.toISOString().slice(0, 10);


        process.data.endDate = endDate;
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

        console.log(`선택일 : ${currentDate.toISOString().slice(0, 10)} 선택월 말일 : ${currentLastDate.toISOString().slice(0, 10)} 종료일 : ${endDate}
                    \n가격 : ${Math.floor(price * duration * 1.1)} 제공건수 : ${Math.floor(offer * duration)}`)

        return `${process.data.salesPrice.toLocaleString()}원 (${process.data.offer.toLocaleString()}건)`
    }
    , setProduct: function () {
        var index = document.querySelector("input[name=product_select]:checked").dataset.index
        process.selectedProduct = productDatalist[index];
    }
}

const utils = {
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