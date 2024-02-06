const preInputParameter = {
    agencyId: "", siteId: "", startDate: "", rateSel: "", openType: "",
}

const init = function () {
    document.querySelector("#selectProductPopUpOpen").addEventListener("click", function () {
        popupOpen(2)
    })
    document.querySelector("#selectProduct").addEventListener("click", function () {
        process.selectProduct();
    })
    document.querySelector("#payment").addEventListener("click", function () {
        payProgress()
        process.setPaymentSiteInfo();
    })
    document.querySelector("#cancelSelectProduct").addEventListener("click", function () {
        popupClose()
    })
    document.querySelector("#cancelPayment").addEventListener("click", function () {
        popupClose()
    })

    process.data.agencyId = utils.getParam("agencyId");
    process.data.siteId = utils.getParam("siteId");

    for (let key in preInputParameter) {
        preInputParameter[key] = utils.getParam(key)
    }

    process.getPaymentInfo();
}

const process = {
    data: {
        agencyId: "", siteId: "", startDate: "", endDate: "", rateSel: "", salesPrice: "", method: ""
    }
    , selectedProduct: {}
    , getPaymentInfo: function () {
        const request = new XMLHttpRequest();
        // request.open("POST", BASE_URL + "/agency/payment/getPaymentInfo", false);
        request.open("POST", BASE_URL + "/agency/payment/getPaymentInfo", false);
        request.setRequestHeader("Content-type", "application/json");
        request.onerror = function () {
            console.log(1)
        }
        request.onabort = function () {
            console.log(1)
        }

        request.onreadystatechange = function () {
            let response = {
                products: "", resultCode: "", resultMsg: ""
            }
            response = JSON.parse(this.response);
            console.log(response);
            if (response.resultCode !== "2000") {
                // alert(response.resultMsg);
                popupTextlist["response"] = {
                    main: response.resultMsg,
                    sub: "",
                    btn: "확인"
                };
                setPopup("response")
                popupOpen(1);
                return;
            }

            setInformation_4({
                companyName: response.companyName, bizNum: response.bizNumber, userName: response.ceoName,
            })

            productDatalist = [];
            Array.prototype.forEach.call(response.products, function (el, index) {
                productDatalist[index] = {
                    productCode: el.code,
                    productName: el.name,
                    productPrice: el.value,
                    productDuration: el.duration,
                    productCount: el.offer,
                    productFeePerCase: el.feePerCase,
                    productExcessFeePerCase: el.excessFeePerCase,
                    productAutopay: el.code.indexOf("autopay") > -1 ? true : false,
                }
            })


            if (response.startDate !== "") {
                document.querySelector("#datepicker").value = response.startDate;
            }

            if (preInputParameter.startDate !== "") {
                document.querySelector("#datepicker").value = preInputParameter.startDate;
            }

            calcPrice = process.select;

            setProductTable(productDatalist)

            if (response.rateSel != null && response.rateSel !== "") {
                setProductCode(response.rateSel);
                selectProduct();
                process.selectProduct();
                setPayment();
            }

            if (preInputParameter.rateSel !== "") {
                setProductCode(preInputParameter.rateSel);
                selectProduct();
                process.selectProduct();
                setPayment();
            }


            // if (response.rateSel != null && response.rateSel !== "") {
            //     process.setProduct();
            //     setPayment();
            // }
            // if (preInputParameter.rateSel !== "") {
            //     process.setProduct();
            //     setPayment();
            // }
        }
        let data = {
            agencyId: process.data.agencyId, siteId: process.data.siteId,
        }
        try {
            request.send(JSON.stringify(data))
        }
        catch (error) {
            alert("서버 통신 불가")
        }
    }
    , setPaymentSiteInfo: function () {
        let request = new XMLHttpRequest();
        request.open("POST", BASE_URL + "/agency/payment/setPaymentSiteInfo", false);
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
    , HFPayment: function (res) {
        var mchtName = "드림시큐리티"
        var mchtEName = "Dreamsecurity"
        var type = "popup"

        if (preInputParameter.openType === "") {
            type = "popup"
        }
        if (preInputParameter.openType === "popup") {
            type = "popup"
        }
        if (preInputParameter.openType === "redirect") {
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
    , selectProduct: function () {
        if (!document.querySelector("input[name=product_select]:checked")) {
            popupTextlist["response"] = {
                main: "상품을 선택해주세요.",
                sub: "",
                btn: "확인"
            };
            setPopup("response")
            popupOpen(1);
            // alert("상품을 선택해주세요.")
            return;
        }
        let index = document.querySelector("input[name=product_select]:checked").dataset.index
        process.selectedProduct = productDatalist[index];
        setPayment()
    }
}

const utils = {
    getParam: function (name) {
        // let search = location.search.substring(location.search.indexOf("?") + 1);
        // let value;

        // search = search.split("&");

        // for (let i = 0; i < search.length; i++) {
        //     let temp = search[i].split("=");
        //     if (temp[0] === name) {
        //         value = temp[1];
        //     }
        // }

        // for (let item of search) {
        //     let temp = item.split("=")
        //     value = temp[0] === name ? temp[1] : ""
        // }

        // return value !== undefined ? decodeURIComponent(value) : value;

        let params = new URLSearchParams(location.search);
        let value = params.get(name)
        return value == null ? "" : decodeURIComponent(value);
    }
}

const thankYouParameter = {
    productName: "", bizNumber: "", companyName: "", startDate: "", autoPay: "",
}

const thankYouInit = function () {
    for (let key in thankYouParameter) {
        thankYouParameter[key] = utils.getParam(key)
    }

    setInformation_5({
        bizNum: thankYouParameter.bizNumber,
        companyName: thankYouParameter.companyName,
        startDate: thankYouParameter.startDate,
        autoPay: thankYouParameter.autoPay === "Y" ? "사용" : "미사용",
        productName: thankYouParameter.productName
    });
}