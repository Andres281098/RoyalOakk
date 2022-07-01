const app = Vue.createApp({

    //CREAR Y USAR VARIABLES
    data() {
        return {
            datos:[],
            clientes:[],
            cliente:[],
            productos:[],
            tipo:[],
            subtipos:[],
            hamburguesas:[],
            ensaladas:[],
            pizzas:[],
            picadas:[],
            clientes:[],
            bebidas:[],
            bebidasSinAlc:[],
            bebidasConAlc:[],
        }
    },

    created(){
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            axios.get('/api/clientes')
            .then(data => {this.clientes = data.data})
            axios.get('/api/clientes/actual')
            .then(data => {this.cliente = data.data})
            console.log(this.cliente)
            axios.get('/api/productos')
            .then(data => {
                this.datos = data.data
                this.productos = this.datos.map(producto=>{
                    let productoNuevo = {
                        id:producto.id,
                        nombre:producto.nombre,
                        descripcion:producto.descripcion,
                        precio:producto.precio,
                        stock:producto.stock,
                        imagen:producto.imagen,
                        tipo:producto.tipo,
                        subTipo:producto.subTipo,
                        vendidos:producto.vendidos,
                        activo:producto.activo,
                        cantidad:0
                    }
                    return productoNuevo
                })
                this.hamburguesas = this.productos.filter(producto => producto.subTipo == 'HAMBURGUESAS')
                this.pizzas = this.productos.filter(producto => producto.subTipo == 'PIZZAS')
                this.picadas = this.productos.filter(producto => producto.subTipo == 'PICADAS')
                this.ensaladas = this.productos.filter(producto => producto.subTipo == 'ENSALADAS')
                this.bebidas = this.productos.filter(producto => producto.tipo == 'BEBIDA')
                this.bebidasConAlc = this.productos.filter(producto => producto.subTipo == 'CON_ALCOHOL')
                this.bebidasSinAlc = this.productos.filter(producto => producto.subTipo == 'SIN_ALCOHOL')
            })
        

    },


    methods: {

        añadirAlPedido(cantidad, idProducto){
            axios.post(`/api/productos/carrito/agregar?cantidad=${cantidad}&idProducto=${idProducto}`,
            { headers: { "content-type": "application/x-www-form-urlencoded" } })
            .then(function (response){
                Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'Producto agregado al pedido',
                    showConfirmButton: false,
                    timer: 1500
                })
            })
            .catch(error =>{
                if(error.response.status == 500){
                    Swal.fire({
                        position: 'center',
                        icon: 'question',
                        title: 'Debes iniciar sesión para realizar un pedido'
                    })
                }
                else if (error.response.status == 400){
                    Swal.fire({
                        position: 'center',
                        icon: 'warning',
                        title: 'Por favor selecciona la cantidad de productos que deseas agregar al pedido'
                    })
                }
            })
        },
        
        sumarProducto(producto){
            let boton = document.querySelector(".stock2")
            if(producto.cantidad < producto.stock){
                producto.cantidad++
                boton.disabled = false
            }
            else if(producto.cantidad > producto.stock){
                boton.disabled = true
            }
        },

        restarProducto(producto){
            let boton = document.querySelector(".stock1")
            if(producto.cantidad > 0){
                producto.cantidad--
                boton.disabled = false
            }
            else if(producto.cantidad < 0){
                boton.disabled = true
            }
        },

        mostrarIngredientes(producto){
            Swal.fire({
                title:`${producto.nombre}`,
                text: `${producto.descripcion}`,
                confirmButtonText: 'Entendido',
                footer: '<a href="">Agregar al carro</a>'
              })
        },

        mostrarCategorias(){
            Swal.fire({
                html:
                  '<div id="pizza">' +
                  '<img src="https://cdn-icons-png.flaticon.com/512/1404/1404945.png" alt="1">' +
                  '<img src="https://cdn-icons-png.flaticon.com/512/198/198416.png" alt="2">' +
        
                        '<img src="https://cdn-icons-png.flaticon.com/512/3076/3076134.png" alt="3">'+
        
                        '<img src="https://cdn-icons-png.flaticon.com/512/1691/1691169.png" alt="4">'+
                                
                        '<img src="https://cdn.pixabay.com/photo/2014/12/21/23/34/swiss-cheese-575541_1280.png" alt="5">'+
        
                        '<img src="https://cdn3.iconfinder.com/data/icons/food-drink-56/100/shushi-512.png" alt="6">'+
        
                        '<img src="https://cdn-icons-png.flaticon.com/512/941/941758.png" alt="7">'+
                                
                        '<img src="https://images.vexels.com/media/users/3/185216/isolated/preview/577e52c9f916cf36bed97b3df404c214-icono-de-papas-fritas.png" alt="8">'+
                                
                        '<img src="https://cdn-icons-png.flaticon.com/512/184/184559.png" alt="9">'+
                                
                        '<img src="https://cdn-icons-png.flaticon.com/512/4515/4515697.png" alt="10">' +                    
        
                        '<img src="https://i.pinimg.com/originals/67/ac/6f/67ac6ff9c411816ed8bbc9ae34368e8f.png" alt="11">' +                   
        
                        '<img src="https://images.vexels.com/media/users/3/143088/isolated/preview/f565debc52083dacca60da22284e4083-icono-de-pierna-de-pollo.png" alt="12">' +                    
                '</div>'
                
              })
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

    },
    

}).mount('#app')


