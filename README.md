# Documentación del Proyecto

Este proyecto es una aplicación desarrollada para Java 8 que se integra con la base de datos de Odoo y requiere una configuración específica para su correcto funcionamiento. Para que la aplicación funcione, es necesario que en la base de datos de Odoo estén instalados los módulos **Project** y **Task Log**. 

Los archivos de propiedades para la configuración de la conexión a la base de datos y el puerto de la aplicación deben estar en las ubicaciones correspondientes:

- **Configuración de PostgreSQL**: Los parámetros de configuración del usuario de PostgreSQL, la IP de la máquina, la contraseña y el número máximo de conexiones se encuentran en el archivo `conexion.properties`, ubicado en el proyecto `RetoServer`, dentro del paquete `dataAccessTier`.
- **Configuración del Puerto**: El puerto que utiliza la aplicación se configura en el archivo `puerto.properties`, ubicado en el proyecto `Library`, dentro del paquete `libreria`.

La aplicación incluye un menú contextual que se activa haciendo clic derecho en diferentes áreas:
- En el **formulario principal**: haciendo clic derecho en el fondo del formulario.
- En la **ventana de inicio**: haciendo clic derecho en cualquier área de la ventana.

Este proyecto fue desarrollado por:
- **Gorka** - GitHub: [GorkaArteaga1](https://github.com/GorkaArteaga1)
- **Oier** - GitHub: [URKIZU](https://github.com/URKIZU)
- **Ekain** - GitHub: [criceyt](https://github.com/criceyt)

Por favor, revisa que los módulos de Odoo y todos los archivos de configuración estén correctamente ubicados y configurados para garantizar el correcto funcionamiento de la aplicación. Para cualquier duda o comentario, puedes contactar a los colaboradores a través de sus perfiles de GitHub.
