### pjatk_jaz_s4_zad3 
### API Restowe dla serwisu filmowego

---

##### Pobieranie filmów
```GET /rest/movie```

##### Pobranie konkretnego filmu
```GET /rest/movie/{id}```

##### Dodawanie filmu
```
POST /rest/movie
{
	"name": "test2",
	"premiere_date": "20.05.2017",
	"description": "opis",
	"genre": "akcja"
}
```

#### Aktualizowanie informacji o filmie
```
PUT /rest/movie
{
	"name": "test2",
	"premiere_date": "20.05.2017",
	"description": "opis",
	"genre": "akcja"
}
```

#### Pobranie komentarzy danego filmu
``` GET /rest/movie/{id}/comments ```

#### Usunięcie danego komentarza
``` DELETE /rest/movie/{id}/comments/{id} ```

#### Dodanie komentarza
``` 
POST /rest/movie/{id}/comment
{
	"text": "comment"
}
```

#### Pobranie oceny danego filmu
```GET /rest/movie/{id}/rate```

#### Dodanie oceny do filmu
```
POST /rest/movie/{id}/rate
{
  "rate": 7
}
```

---

#### AKTORZY

#### Dodanie aktora
```
POST /rest/actors
{
	"name": "Mila Kunis",
	"gender": "m"
}
```

#### Pobranie aktora
```GET /rest/actor/{id}```

#### Pobranie aktorów
```GET /rest/actor```

#### Dodanie aktora do filmu
```
POST /rest/movie/{id}/actors
{
	"name": "Mila Kunis",
	"gender": "m"
}
```

#### Przydzielenie filmu do danego aktora
```
PUT /rest/actor/{id}/movie/{id}
{
	"name": "test2",
	"premiere_date": "20.05.2017",
	"description": "opis",
	"genre": "akcja"
}
```

#### Wyświetlenie filmów danego aktora
```GET /rest/actor/{id}/movies```

#### Wyświetlenie aktorów danego filmu
``` GET /rest/movie/{id}/actors```

