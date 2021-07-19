# Protic
Protic es una aplicación que proporciona a estudiantes y trabajadores
del sector de las tecnologías de la información y la comunicación un modo de evaluar cuál es el rango salarial justo
para una determinada profesión u oferta de trabajo teniendo en cuenta la empresa donde se realizará la labor,
el puesto de trabajo y las tecnologías relacionadas con el puesto mediante el intercambio de experiencias laborales
entre usuarios, permitiendo a éstos mantener el control sobre la privacidad de sus datos y de su identidad.

Más información aquí: [Memoria TFG](https://github.com/vicengg/protic/blob/main/doc/memoria.pdf)

## Instalación
1. Clonar el repositorio:
```git clone https://github.com/vicengg/protic.git```
2. Arrancar el servidor:
```
cd protic
mvn spring-boot:run
```
3. Arrancar la aplicación web (en otro terminal):
```
cd protic-app
npm start
```
4. Acceso a la aplicación: en el navegador introducir
```http://localhost:8083/```, ingresar con el usuario de GitHub.


## Introducción a la aplicación
Una de las dificultades a las que se enfrentan los alumnos en período de prácticas empresariales
o recién egresados de la universidad es el desconocimiento de qué salario podría
considerarse justo para un determinado puesto ante una oferta de trabajo. A esta dificultad se
añade el hecho de que muchos trabajadores consideran hablar de su sueldo un tema tabú.
El objetivo de este Trabajo Fin de Grado es desarrollar una aplicación que proporcione a
estudiantes y trabajadores del sector de las tecnologías de la información y la comunicación un
modo de evaluar cuál es el rango salarial justo para una determinada profesión u oferta de trabajo
teniendo en cuenta la empresa donde se realizará la labor, el puesto de trabajo y las tecnologías
relacionadas con el puesto mediante el intercambio de experiencias laborales entre usuarios,
permitiendo a éstos mantener el control sobre la privacidad de sus datos y de su identidad.
El proyecto consiste en una aplicación web desarrollada con la librería de JavaScript React
y un servidor de aplicaciones escrito en Java con Sprint Boot siguiendo el patrón de arquitectura
hexagonal. El resultado es una aplicación que puede ejecutarse en un navegador web y que hace
uso de los servicios de intercambio de experiencias laborales que proporciona el servidor. El
servidor además está integrado con la plataforma GitHub como proveedor de autenticación
y de datos de usuarios, por lo que cualquier usuario con cuenta de GitHub puede hacer uso de
la plataforma.

## Diagrama de arquitectura

![Diagarama de arquitectura](https://github.com/vicengg/protic/blob/main/doc/img/Arquitectura_hexagonal.png?raw=true "Diagrama de arquitectura")

## 