Vue.createApp({
    data() {
        return {
            nombreCliente: "",
            apellidoCliente: "",
            mailCliente: "",
            telefonoCliente: "",
            direccionCliente: [],
            direccionNueva: [],
            contrasenaCliente: "",
            cliente: [],

        }
    },
    created() {
        axios.get('/api/clientes/actual')
            .then(datos => {
                this.nombreCliente = datos.data.nombre
                this.apellidoCliente = datos.data.apellido
                this.mailCliente = datos.data.email
                this.telefonoCliente = datos.data.telefono
                this.direccionCliente = datos.data.direcciones
                this.contrasenaCliente = datos.data.contraseña
                this.cliente = datos.data
            })


    },
    methods: {

        editarCliente() {

            let setDirecciones = this.direccionCliente.concat(this.direccionNueva)

            Swal.fire({
                title: "Estas seguro de querer modificar los datos?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#014377',
                cancelButtonColor: '#ff0000',
                confirmButtonText: 'Confirmar!'
            })
                .then((result) => {
                    if (result.isConfirmed) {

                        axios.patch('/api/clientes/actual/modificar', `nombre=${this.nombreCliente}&apellido=${this.apellidoCliente}&email=${this.mailCliente}&telefono=${this.telefonoCliente}&direccion=${setDirecciones}&contraseña=${this.contrasenaCliente}`,
                            { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                            .then(() => {
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Exito!',
                                    text: 'Datos Modificados!',
                                    timer: 3000
                                })
                                    .then(() => location.reload())
                            })
                            .catch((error) => {
                                Swal.fire({
                                    icon: 'error',
                                    text: error.response.data,
                                })
                            })

                    }
                })
        },

        confirmarContraseña() {
            Swal.fire({
                html: ``,
                title: 'Ingrese su contraseña',
                icon: 'warning',
            })
        },

        eliminarDireccion(param) {
            Swal.fire({
                title: "Estas seguro de querer eliminar esta direccion?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#014377',
                cancelButtonColor: '#ff0000',
                confirmButtonText: 'Confirmar!'
            })
                .then((result) => {
                    if (result.isConfirmed) {
                        axios.delete(`/api/clientes/actual/eliminarDireccion?direccionBorrada=${param}&contraseña=${this.contrasenaCliente}`)
                            .then(() => {

                                Swal.fire({
                                    icon: 'success',
                                    title: 'Exito!',
                                    text: 'La direccion ha sido eliminada!',
                                    timer: 3000
                                })
                                    .then(() => location.reload())
                            })
                            .catch((error) => {
                                console.log(error.response.data)
                                Swal.fire({
                                    icon: 'error',
                                    text: error.response.data,
                                })
                                    .then(() => location.reload())
                            })

                    }

                })



        },
        salir() {
            Swal.fire({
                title: '¿Estas seguro que quieres cerrar sesion?',
                text: 'Si cierras sesion solo podras ver nuestra seccion de productos pero no podras ordenar tu compra',
                popup: '',
                icon: 'warning',
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