Vue.createApp({
    data() {
        return {
            factura:[],
            totalFactura:"",
            apiHbanking:"https://homebanking-mh.herokuapp.com/api/transaction/payments",
            numUno:"",
            numDos:"",
            numTres:"",
            numCuatro:"",
            nombre:"",
            cvv:"",
            nombreTitular:"",
            fechaMes:"",
            fechaAño:"",
        }
    },
    created() {
        axios.get("/api/clientes/actual")
            .then(datos =>{
                this.factura = datos.data.facturas[0]
                this.totalFactura = this.factura.total
            })
    },
    methods: {
        confirmacionPagoFunc(){
            let numTarjeta = this.numUno+this.numDos+this.numTres+this.numCuatro;
            axios.post('https://homebanking-mh.herokuapp.com/api/transactions/payments',    {
                "cardNumber": "1234123412341234",
                "securityCode": "856",
                "amount": this.totalFactura,
                "detail": "N° Factura: " + this.factura.id
                })
                .then(data =>{
                    Swal.fire({
                        title: "Compra exitosa",
                        text: "¡Gracias por tu cumpra!",
                        icon: "success",
                        confirmButtonColor: '#12A098',
                        confirmButtonText: "Vamos por la comida!🍔",
                        width: "40%",
                    })
                    .then(response =>{
                        window.location.href="/api/clientes/factura"
                    })
                    .then(
                        axios.post("/api/clientes/factura")
                            // .catch(error =>{
                            //     Swal.fire({
                            //         title: "Fallo descarga factura",
                            //         text: "No se pudo descargar tu factura, no te preocupes en un momento la encontraras en tu perfil",
                            //         icon: "error",
                            //         width: "30%",
                            //     })
                            // })
                    )

                })
                .catch(error =>{
                    console.log(error.response)
                    Swal.fire({
                        title: "Fallo de pago",
                        text: "Error en el procesamiento de tu pago, vuelve a intentar",
                        icon: "error",
                        width: "30%",
                    })
                })

        }

    },

    computed: {
        
    }
}).mount('#app')