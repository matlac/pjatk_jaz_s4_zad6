### pjatk_jaz_s4_zad6
### API Restowe dla sklepu komputerowego

---

##### Pobieranie produktów
```GET /rest/product```

##### Pobranie konkretnego produktu
```GET /rest/product/{id}```

##### Dodawanie produktu
```
POST /rest/product
{
	"name": "komputer",
	"description": "opis",
	"amount": 2230,
	"category": "Ram"
}
```

#### Aktualizowanie informacji o produkcie
```
PUT /rest/product
{
	"name": "komputer",
	"description": "opis",
	"amount": 2230,
	"category": "Ram"
}
```

---

### KOMENTARZE

##### Pobieranie komentarzy
```GET /rest/comment```

##### Pobranie konkretnego komentarza
```GET /rest/comment/{id}```

##### Usunięcie konkretnego komentarza
```DELETE /rest/comment/{id}```

#### Pobranie komentarzy danego produktu
``` GET /rest/product/{id}/comments ```

#### Usunięcie danego komentarza
``` DELETE /rest/product/{id}/comments/{id} ```

#### Dodanie komentarza
``` 
POST /rest/product/{id}/comment
{
	"text": "comment"
}
```

---

#### WYSZUKIWANIE

#### Dodanie aktora
```
GET /rest/product?{argument=value}&&[...]
example: /rest/product/search?amount_from=20&name=test
```

#### Dostępne filtry
amount_from | >, int
amount_to | >, int
name | like, string
category | like, string


