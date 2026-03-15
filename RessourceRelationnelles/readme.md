Utilisation de jakarta pour écrire notre API et lombok
Class :
@Data permet de ne pas écrire les getters et setters
@Entity indique que c'est une table
@Table(name = "xxx") associe une table

Contenu :
@Id fait l'association avec l'id en BDD
@GeneratedValue(strategy = GenerationType.IDENTITY) auto incrémenté

@Column(name="") lie un paramètre à une colonne uniquement si champ différent du nom de la colonne

Structure d'un repository :

- save(S entity) : Si l'ID est vide, il fait un INSERT. Si l'ID existe déjà, il fait un UPDATE.
- findById(ID id) : Cherche une ligne par sa clé primaire (renvoie un Optional).
- findAll() : Récupère toutes les lignes de la table (renvoie une List).
- existsById(ID id) : Renvoie un booléen (pratique pour vérifier avant de créer).
- count() : Renvoie le nombre total de lignes.
- deleteById(ID id) : Supprime la ligne correspondante.

```
package cesi.RessourceRelationnelles.repositories;

import org.springframework.data.jpa.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cesi.RessourceRelationnelles.models.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

}
```
