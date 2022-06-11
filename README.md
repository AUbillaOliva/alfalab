# Alfalab

Alfalab es un taller de revelado fotográfico analógico ubicado en Santiago de Chile.

Este proyecto consiste en un sistema de gestión de información del taller para el manejo de pedidos.

El proyecto en si se divide en 3 partes:

- Android: Aplicación para dispositivos moviles con sistema operativo Android.
- Web: Aplicación web hecha en NextJS
- Api: API CRUD

---

## Modelo de Pedidos (Order)

### Atributos

`order_list`: Lista de rollos

- `film`: Tipo de pelicula
- `format`: Tipo de formato
- `digitized`: Opciones de digitalizado

  - `quality`: Calidad del digitalizado
  - `format`: Tipo de archivo de digitalizado
  - `price`: Precio de digitalizado

- `level`: Opciones de forzado

  - `level`: Pertenece al forzado del rollo fotográfico. El valor es menor o igual a 4
  - `price`: Precio correspondiente al forzado

- `responsible`: Usuario a cargo
- `status`: Estado del rollo
- `price`: Precio del rollo

`client`: Datos del cliente

`last_edit`: Ultima fecha de edición

`delivered_date`: Fecha de entrega

`comments`: Comentarios

- `author`: Autor del comentario
- `date`: Fecha de creación
- `message`: Contenido del comentario

`price`: Precio

---

### Esquema

\_id `string | Types.ObjectId('Order')`

**order_list** `[required] IOrderItem[]`

- **film** `[required] string | Types.ObjectId('Film')`
- **format** `[required] string | Types.ObjectId('Format')`
- **responsible** `[required] string | Types.ObjectId('User')`
- **price** `[required] number`
- digitized `IDigitized`
  - **quality** `[required] 'Media' | 'Alta' | string`
  - **format** `'[required] JPG' | 'TIF' | string`
  - **price** `[required] number`
- level `ILevel`
  - **level** `[required] number`
  - **price** `[required] number`
- status `'delivered' | 'not_sent' | 'received_by_client' | 'Otro'| string`

**client** `[required] IClient`

- **name** `[required] string`
- **email** `[required] string`
- instagram `string`
- phone `string`

**last_edit** `[required] string | Types.ObjectId('User')`

**price** `[required] price`

**created_at** `[required] Date`

comments `IComment[]`

- **author** `[required] string | Types.ObjectId('User')`
- **date** `[required] Date`
- **message** `[required] string`

delivered_date `Date`

---

### Lista de precios

- `Level`
  | Level | Precio |
  | ----- | ------ |
  | 1 | 0 |
  | 2 | 1000 |
  | 3 | 2000 |
  | 4 | 3000 |

  El precio del forzado se calcula con la sgte. formula: (level-1)\*1000

- `Digitized`
  | Digitalizado | JPG | TIF\* |
  | ------- | ------- | ------ |
  | **ALTA** | 4000 | 5500 |
  | **MEDIA** | 2500 | 4000 |

  **IMPORTANTE**: Si el formato del rollo a digitalizar es 110, entonces la propiedad `quality` debe ser 'Alta', Al precio total del rollo a digitalizar, se le descuenta 500 por promo.

---

## Modelo de Tipos de Pelicula (FilmType)

### Atributos - FilmType

`film_type`: El el tipo de pelicula

`price`: El precio asociado al tipo de película

---

### Esquema - FilmType

\_id `string | Types.ObjectId`

film_type `'Cine' | 'Color' | 'ByN' | 'Diapo' | string`

price `number`

---

### Lista de precios - FilmType

| -----   | Color/Diapo | Cine | ByN     |
| ------- | ----------- | ---- | ------- |
| Formato | 110/120/135 | 135  | 120/135 |
| Precio  | 2500        | 3500 | 3000    |

---

## Modelo de Tipos de Formatos (Format)

### Atributos - Format

`format_type`: El el tipo de formato de formato

`price`: El precio asociado al tipo de formato

---

### Esquema - Format

\_id `string | Types.ObjectId`

format_type `[required] '135' | '120' | '110' | string`

price `[required] number`

---

### Lista de precios - Format

| -----    | 110/120/135 | 135  | 120/135 |
| -------- | ----------- | ---- | ------- |
| FilmType | Color/Diapo | Cine | ByN     |
| Precio   | 2500        | 3500 | 3000    |
