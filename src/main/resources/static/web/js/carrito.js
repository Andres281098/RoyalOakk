Vue.createApp({
    data(
    ) {
        return {
            clientes:[],
            cliente: [],
            facturas:[],
            pedidos:[],
            envioValue:"",
            subtotalTotal:0,
            productos:[],
            modificarCantidad: 0,
            idProducto:0,
        }
    },
    created() {
        axios.get('/api/clientes')
        .then(datos => this.clientes= datos.data)
        axios.get('/api/clientes/actual')
        .then(datos => {
            this.cliente = datos.data
            this.facturas = this.cliente.facturas.sort((f1,f2) => f1.id - f2.id);
            this.facturas = this.facturas.filter(factura => factura.estadoFactura == 'CARRITO')
            this.pedidos = this.facturas[0].pedidos.sort((p1,p2)=> p1.id - p2.id);
            this.subtotalTotal = this.facturas[0].total
        })
    },
    methods: {
        modificarPedido(){
            axios.patch(`/api/productos/carrito/modificar?nuevaCantidad=${this.modificarCantidad}&idProducto=${this.idProducto}`)
            .then(function (response){
                window.location.href = "/web/pages/carrito.html"
            })
            .catch(function (error){
                console.log(error)
            })
        },

        borrarPedido(id){
            axios.delete(`/api/productos/carrito/borrar?idPedido=${id}`,
            { headers: { "content-type": "application/x-www-form-urlencoded" } })
            .then(function (response){
                window.location.href = "/web/pages/carrito.html"
            })
            .catch(function (error){
                console.log(error)
            })
        },

        sumarPedido(pedido){
            let boton = document.querySelector(".stock2")
            if(pedido.cantidad < pedido.stockProducto){
                pedido.cantidad++
                boton.disabled = false
            }
            else if(pedido.cantidad > pedido.stockProducto){
                boton.disabled = true
            }
            this.modificarCantidad = pedido.cantidad
            this.idProducto = pedido.idProducto
        },
        
        quitarPedido(pedido){
            let boton = document.querySelector(".stock1")
            if(pedido.cantidad > 1){
                pedido.cantidad--
                boton.disabled = false
            }
            else if(pedido.cantidad < 1){
                boton.disabled = true
            }
            this.modificarCantidad = pedido.cantidad
            this.idProducto = pedido.idProducto
        },

        salir(){
            Swal.fire({
                title:'¿Estas seguro que quieres cerrar sesion?',
                text: 'Si cierras sesion solo podras ver nuestra seccion de productos pero no podras ordenar tu compra',
                popup: '',
                icon:'warning',
                confirmButtonColor: '#12A098',
                cancelButtonColor: '#d33',
                confirmButtonText: true,
                confirmButtonText: 'Si, salir',
                showCancelButton: true,
                cancelButtonText: 'No, volver!',    
                })
                .then((result) => {
                    if (result.isConfirmed) {
                        axios.post('/api/logout')
                        window.location.href = './login.html'
                    }
                  })
        }

    },

    computed: {
    }
}).mount('#app')