// Ahora, tanto el valor mínimo como el máximo están incluidos en el resultado.
function idRandom(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1) + min);
  }
const app = Vue.createApp({

    //CREAR Y USAR VARIABLES
    data() {
        return {
            cliente:[],
            num: 12,
            cantidad: 1,
            cantComida: 1,
            cantBebida:0,
            productos: [],
            productosRandom: [],
            seccionAleatoria: false,
            totalPagar : 0,
            comidas : [],
            bebidas : [],
            deshabilitar : true,
            agregado:0
        }
    },

    created(){
        axios.get('/api/clientes/actual')
        .then(data => {this.cliente = data.data})

        axios.get('/api/productos')
            .then(respose=>{
                this.productos = respose.data
                this.comidas = this.productos.filter(producto => producto.tipo == 'COMIDA' && producto.activo)
                this.bebidas = this.productos.filter(producto => producto.tipo == 'BEBIDA' && producto.activo)
            })
    },


    methods: {
        aumentarCantidad(param){
            if(param === 'comida'){
                this.cantComida = this.cantComida + 1
            }else if (param === 'bebida'){
                this.cantBebida = this.cantBebida + 1
            } else if(param.cant < param.stock){
                param.cant ++
            }
        },
        disminuirCantidad(param){
            if(this.cantidad >1) {this.cantidad = this.cantidad -1}
            if(param === 'comida' && this.cantComida > 1){
                this.cantComida = this.cantComida - 1
            }else if (param === 'bebida' && this.cantBebida > 0){
                this.cantBebida = this.cantBebida - 1
            }else if (param.cant > 1){
                param.cant --
            }
        },
        random(){
            this.totalPagar = 0
            this.productosRandom = []
            this.seccionAleatoria = true
            let num
            let ids = []
            let ok
            this.seccionRandom = false
            for (let index = 0; index < this.cantComida; index++) {
                ok = true
                while(ok){
                    num = idRandom(1,this.comidas.length)
                    if(!ids.includes(num) && this.comidas.find(product=>product.id == num) != undefined){
                        this.productosRandom.push(this.comidas.find(producto => producto.id == num))
                        ids.push(num)
                        ok = false
                    }
                }
            }
            for (let index = 0; index < this.cantBebida; index++) {
                ok = true
                while(ok){
                    num = idRandom(38,53)
                    if(!ids.includes(num) && this.bebidas.find(product=>product.id == num) != undefined){
                        this.productosRandom.push(this.bebidas.find(producto => producto.id == num))
                        ids.push(num)
                        ok = false
                    }
                }
            }
            this.productosRandom.forEach(producto => {
                producto.cant = 1
            })
        },
        eliminarProducto(producto){
            this.productosRandom = this.productosRandom.filter(product => product != producto)
        },
        agregar(producto){
                axios.post('/api/productos/carrito/agregar',`cantidad=${producto.cant}&idProducto=${producto.id}`)
                    .then(response => {
                        let elem = document.querySelector(`#btn-${producto.id}`)
                        elem.innerHTML = '<div class="btn-group align-self-center" role="group" aria-label="First group"><button  type="button" disabled class="btn btn-outline-secondary stock2 d-flex justify-content-center align-items-center"><i class="material-icons">check</i> Añadido al carrito</button></div>'
                        this.agregado++
                    })
                    .catch(error => console.log('no añadido '+producto.nombre))
        },
        verCarrito(){
            window.location.href = './carrito.html'
        },
        revisarOtraVez(){
            this.seccionAleatoria = false
        }

    },

    computed: {
        total(){
            this.totalPagar = 0
            this.productosRandom.forEach(producto => {
                this.totalPagar = this.totalPagar + (producto.precio * producto.cant)
            })
        }
    },
    

}).mount('#app')
console.log('holaa')



