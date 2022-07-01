const app = Vue.createApp({

    //CREAR Y USAR VARIABLES
    data() {
        return {
            nombre:"",
            descripcion:"",
            imagen:"",
            precio:"",
            stock:"",
            tipo:"",
            subTipo:"",
            activo:""



        }
    },

    created(){
            
        

    },


    methods: {
        
        crearProducto(){
            console.log(this.nombre,this.descripcion,this.imagen,this.precio,this.stock,this.tipo,this.subTipo,this.activo)
            axios.post('http://localhost:8585/api/productos',`nombre=${this.nombre}&descripcion=${this.descripcion}&imagen=${this.imagen}&precio=${this.precio}&stock=${this.stock}&tipo=${this.tipo}&subtipo=${this.subTipo}&activo=${this.activo}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                console.log("producto creado con exito")
                
                Swal.fire({
                    title: "Producto creado",
                    text: "Tu nuevo producto fue creado exitosamente",
                    icon: "success",
                    confirmButtonColor: '#12A098',
                    confirmButtonText: "Okey!🍔",
                    width: "40%",
                })
                .then(response => {
                    window.location.reload()
                })
            })
            .catch(response => {
                console.log("error")
                Swal.fire({
                    title: "Fallo al crear",
                    text: "Parte de tu información no es correcta. Intentalo de nuevo",
                    icon: "error",
                    confirmButtonText: "intentar de nuevo",
                    width: "30%",
                })
            })
        }
                    
    },

    computed: {

    },
    

}).mount('#app')


