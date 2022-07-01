const app = Vue.createApp({

    //CREAR Y USAR VARIABLES
    data() {
        return {
            productos:[],
            hamburguesas:[],
            tipo:[],
            subtipos:[],

            cliente:[],

            hamburguesas:[],
            picadas:[],
            pizzas:[],
            comida:[],
            ensaladas:[],
            bebidasSA:[],
            bebidasCA:[],

        }
    },

    created(){
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            axios.get('http://localhost:8585/api/productos')
            .then(data => {
                this.productos = data.data

                this.hamburguesas = this.productos.filter(productos=> productos.subTipo == 'HAMBURGUESAS')
                this.pizzas = this.productos.filter(productos=> productos.subTipo=='PIZZAS')
                this.picadas = this.productos.filter(productos=> productos.subTipo=='PICADAS')
                this.ensaladas = this.productos.filter(productos=> productos.subTipo=='ENSALADAS')
                this.bebidasSA = this.productos.filter(productos=> productos.subTipo=='SIN_ALCOHOL')
                this.bebidasCO = this.productos.filter(productos=> productos.subTipo=='CON_ALCOHOL')
                console.log(this.productos)
                console.log(this.hamburguesas)
                console.log(this.pizzas)
                console.log(this.picadas)
                console.log(this.ensaladas)
                console.log(this.bebidasSA)
                console.log(this.bebidasCA)

            })
            
            /* axios.get('http://localhost:8585/api/clientes')
            .then(data => {
                this.cliente = data.data
                this.email = this.cliente.email
            }) */
        

    },



    methods: {
        
        mostrarIngredientes(producto){
            Swal.fire({
                title:`${producto.nombre}`,
                text: `${producto.descripcion}`,
                confirmButtonText: 'Entendido',
                footer: '<a href="">Ok</a>'
              })
        },

        eliminarProducto(id){
            Swal.fire({
                title:'Confirmar eliminacion',
                text: '¿Estas seguro que quieres eliminar este producto?',
                icon:'warning',
                confirmButtonColor: '#12A098',
                cancelButtonColor: '#d33',
                confirmButtonText: true,
                confirmButtonText: 'Eliminar',
                showCancelButton: true,
                cancelButtonText: 'Cancelar',
                })
                .then((result) => {
                    if (result.isConfirmed) {
                        axios.delete(`http://localhost:8585/api/productos?idProducto=${id}`)
                        .then(response =>{
                            console.log("delete succcesfull")
                            Swal.fire({
                                title:'Producto Eliminado',
                                text: 'producto eliminado correctamente',
                                icon:'success',
                                confirmButtonColor: '#12A098',
                                cancelButtonColor: '#d33',
                                confirmButtonText: true,
                                confirmButtonText: 'Ok',
                                })
                                .then( result =>{
                                    location.reload()    
                                })
                        })
                        .catch(error => 
                            console.log("error"))
                            Swal.fire({
                                title:'Error',
                                text: 'error al eliminar el producto',
                                icon:'error',
                                confirmButtonColor: '#12A098',
                                cancelButtonColor: '#d33',
                                confirmButtonText: true,
                                confirmButtonText: 'Ok',
                                })
                    }
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


