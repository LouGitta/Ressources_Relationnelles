package cesi.RessourceRelationnelles;

import cesi.RessourceRelationnelles.controllers.*;
import cesi.RessourceRelationnelles.models.Relation;
import cesi.RessourceRelationnelles.models.Ressource;
import cesi.RessourceRelationnelles.models.RessourceStatus;
import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.User;
import cesi.RessourceRelationnelles.models.Type;
import cesi.RessourceRelationnelles.models.Category;
import cesi.RessourceRelationnelles.models.Visibility;
import cesi.RessourceRelationnelles.models.Role;
import cesi.RessourceRelationnelles.models.Friend;
import cesi.RessourceRelationnelles.models.FriendStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.thymeleaf.expression.Lists;

@SpringBootTest
class RessourceRelationnellesApplicationTests {

	@Autowired
	public UserController userControl;
	@Autowired
	public TypeController typeControl;
	@Autowired
	public CommentController commentControl;
	@Autowired
	public CategoryController categoryControl;
	@Autowired
	public FriendController friendControl;
	@Autowired
	public ProgressionController progressionControl;
	@Autowired
	public RessourceController ressourceControl;
	@Autowired
	public RelationController relationControl;

	@Test
	@Order(10)
	void SupprimerUtilisateur(){
		var users = userControl.getAll().getBody();
		User user1 = null;
		User user2 = null;

		for (User user : users) {
			var name = user.getUsername();
			if(name.equals("Johanes 1er du nom"))
			{
				user1 = user;
			} 
			if(name.equals("Johanes 2eme du nom"))
			{
				user2 = user;
			}
		}
		var friendList = new ArrayList<Friend>();
		var friendsUser2 = friendControl.getAllFriendAndRequests(user2.getId()).getBody();
		var friendsUser1 = friendControl.getAllFriendAndRequests(user1.getId()).getBody();

		friendList.addAll(friendsUser1);
		friendList.addAll(friendsUser2);


		for (Friend friend : friendList) {
			friendControl.delete(friend.getId());			
		}

		friendsUser2 = friendControl.getAllFriend(user2.getId()).getBody();
		friendsUser1 = friendControl.getAllFriend(user1.getId()).getBody();

		userControl.delete(user1.getId());
		userControl.delete(user2.getId());


		users = userControl.getAll().getBody();
		Boolean isDeleted = true;
		for (User user : users) {
			var name = user.getUsername();
			if(name.equals("Johanes 1er du nom") )
			{
				isDeleted = false;
			} 
			if(name.equals("Johanes 2eme du nom"))
			{
				isDeleted = false;
			} 
		}
		Assert.isTrue(isDeleted,"The users have not been deleted");
	}

	
	@Test
	@Order(2)
	void CreerUtilisateurMauvais(){
		//créer utilisateurs trop court
		var user1 = new User(0, "PasSafe", "pasSafe@gmail.com", "PasSafe", Role.citizen, LocalDateTime.now(),
            true);
		Boolean isAdded = false;

		try {
			var user1WasCreated = userControl.create(user1).getBody() == user1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			var users = userControl.getAll().getBody();
			for (User user : users) {
				var name = user.getUsername();
				if(name.equals("PasSafe") )
				{
					isAdded = true;
					userControl.delete(user.getId());
				}
			}
		}

		Assert.isTrue(isAdded,"The user has been created with an invalid password");
	}

	

	@Test
	@Order(3)
	void ModifierUtilisateur(){
		var users = userControl.getAll().getBody();
		User user1 = null;
		User user2 = null;

		for (User user : users) {
			var name = user.getUsername();
			if(name.equals("Johanes 1er du nom") )
			{
				user1 = user;
			} 
			if(name.equals("Johanes 2eme du nom"))
			{
				user2 = user;
			}
		}

		user1.setPassword("1NewPassword@cesi1235813");
		user2.setPassword("1NewPassword@cesi1235813");
		var user1WasUpdated = userControl.update(user1.getId(),user1).getBody().getId() == user1.getId();
		var user2WasUpdated = userControl.update(user2.getId(),user2).getBody().getId() == user2.getId();

		Assert.isTrue(user1WasUpdated && user2WasUpdated,"At least one of the two users has not been updated");

	}


	@Test
	@Order(9)
	void DeleteRessourceAsModerator(){
		var ressources = ressourceControl.getAll().getBody();
		Ressource res1 = null;
		Ressource res2 = null;
		var lstRes = new ArrayList<Ressource>();
		for (Ressource res : ressources) {
			var title = res.getTitle();
			if (!title.equals("Comment mieux communiquer avec ses enfants") 
				&& !title.equals("Le jeu des 7 familles des émotions") && !title.equals("Mes réflexions sur le monde pro")) {
				lstRes.add(res);
			}
		}

		for (Ressource ressource : lstRes) {
			ressourceControl.delete(ressource.getId());
		}

		ressources = ressourceControl.getAll().getBody();
		Boolean isDeleted = true;

		for (Ressource res : ressources) {
			if (res.getTitle().equals("Ressource1")) {
				isDeleted = false;
			}
			if (res.getTitle().equals("Ressource2")) {
				isDeleted = false;
			}
		}
	}

	@Test
	@Order(8)
	void ValidateRessourceAsModerator(){
		var users = userControl.getAll().getBody();
		User user1 = null;
		for (User user : users) {
			if (user.getRole() == Role.moderator) {
				user1 = user;
			}
		}
		var ressources = ressourceControl.getAll().getBody();
		Ressource res1 = null;
		for (Ressource res : ressources) {
			if (res.getStatus() == RessourceStatus.pending) {
				res1 = res;
			}
		}
		res1.setStatus(RessourceStatus.published);
		var ressource1WasValidated = ressourceControl.update(res1.getId(),res1).getBody().getStatus() == RessourceStatus.published;
		Assert.isTrue(ressource1WasValidated,"The ressource has not been validated");

	}

	@Test
	@Order(7)
	void ModifierRessourceAsCitizen(){
		var users = userControl.getAll().getBody();
		User user1 = null;
		for (User user : users) {
			if (user.getRole() == Role.citizen) {
				user1 = user;
			}
		}
		var ressources = ressourceControl.getAll().getBody();
		Ressource res1 = null;
		for (Ressource res : ressources) {
			if (res.getTitle().equals("Ressource1")) {
				res1 = res;
			}
		}
		res1.setTitle("ModifiedRessource1") ;
		var ressource1WasCreated = ressourceControl.update(res1.getId(),res1).getBody().getTitle().equals("ModifiedRessource1");

		Assert.isTrue(ressource1WasCreated,"The ressource has not been modified");
	}
	
	@Test
	@Order(0)
	void CreerRessourceAsCitizen(){
		var users = userControl.getAll().getBody();
		User user1 = null;
		for (User user : users) {
			if (user.getRole() == Role.citizen) {
				user1 = user;
			}
		}

				var categories = categoryControl.getAll().getBody();
		Category category1 = categories.get(0);


				var types = typeControl.getAll().getBody();
		Type type1 = types.get(0);

				var relations = relationControl.getAll().getBody();
		Relation relation1 = relations.get(0);

		var res1 = new Ressource(0, "Ressource1", "contenu1", 0, relation1, type1,
            category1,Visibility.public_visibility, RessourceStatus.pending, LocalDateTime.now());
		var res2 = new Ressource(0, "Ressource2", "contenu2", 0, relation1, type1,
            category1,Visibility.public_visibility, RessourceStatus.pending, LocalDateTime.now());
			res1.setUser(user1);
			res2.setUser(user1);

		var ressource1WasCreated = ressourceControl.create(res1).getBody() == res1;
		var ressource2WasCreated = ressourceControl.create(res2).getBody() == res2;

		Assert.isTrue(ressource1WasCreated && ressource2WasCreated,"At least one of the two ressources has not been created");
	}

	@Test
	@Order(5)
	void RefuseFriendRequest(){
		var users = userControl.getAll().getBody();
		User user1 = null;
		User user2 = null;
		for (User user : users) {
			if (users.indexOf(user) == 0) {
				user1 = user;
			}
			if (users.indexOf(user) == 1) {
				user2 = user;
			}
		}

		var friend1 = new Friend(0,user1,user2,FriendStatus.pending,LocalDateTime.now());
		var friendFound = friendControl.create(friend1).getBody();

		friendFound.setStatus(FriendStatus.rejected);
		var body = friendControl.update(friend1.getId(),friend1).getBody().getStatus();
		var AddAsFriend =  body == FriendStatus.rejected;
		Assert.isTrue(AddAsFriend, "Echec du refus d'amis");
		friendControl.delete(friendFound.getId());

	}
	

	@Test
	@Order(6)
	void AcceptFriendRequest(){
		var users = userControl.getAll().getBody();
		User user1 = null;
		User user2 = null;
		for (User user : users) {
			if (users.indexOf(user) == 0) {
				user1 = user;
			}
			if (users.indexOf(user) == 1) {
				user2 = user;
			}
		}

		var friend1 = new Friend(0,user1,user2,FriendStatus.pending,LocalDateTime.now());
		var friendFound = friendControl.create(friend1).getBody();

		friendFound.setStatus(FriendStatus.accepted);
		var body = friendControl.update(friend1.getId(),friend1).getBody().getStatus();
		var AddAsFriend = body == FriendStatus.accepted;
		Assert.isTrue(AddAsFriend, "Echec de l'acceptation d'amis");
		friendControl.delete(friendFound.getId());

	}

	@Test
	@Order(4)
	void AddAsFriend(){
		var users = userControl.getAll().getBody();
		User user1 = users.get(0);
		User user2 = users.get(1);
		//for (User user : users) {
		//	if(user.getUsername().equals("Johanes 1er du nom")) user1 = user;
		//	if(user.getUsername().equals("Johanes 2eme du nom")) user2 = user;
//
		//}

		var friend = new Friend(0, user1, user2, FriendStatus.pending, LocalDateTime.now());
		var friendFound = friendControl.create(friend).getBody();
		var AddAsFriend = friendFound.getUser1().getId()== friend.getUser1().getId() && friendFound.getUser2().getId()== friendFound.getUser2().getId() ;
		Assert.isTrue(AddAsFriend, "Echec de la demande d'amis");
		friendControl.delete(friendFound.getId());
	}

	@Test void CreateTooLateRessource(){

	}

	@Test void CreateActivityAsUser(){

	}

	@Test void CreateActivityAsCitoyen(){

	}	
	@Test void CreateActivityAsAdmin(){

	}	
	@Test void CreateActivityAsModerator(){

	}

	@Test void DeleteActivity(){

	}

	@Test void DeleteCommentFromUser(){

	}

	@Test void CreateCategory(){

	}

	@Test
	@Order(1)
	void CreerUtilisateur(){
		try {
			var users = userControl.getAll().getBody();
			User user1 = null;
			User user2 = null;
			for (User user : users) {
				var name = user.getUsername();
				if(name.equals("Johanes 1er du nom") )
				{
					user1 = user;
				} 
				if(name.equals("Johanes 2eme du nom"))
				{
					user2 = user;
				}
			}
		var friendList = new ArrayList<Friend>();
		var friendsUser2 = friendControl.getAllFriendAndRequests(user2.getId()).getBody();
		var friendsUser1 = friendControl.getAllFriendAndRequests(user1.getId()).getBody();
		friendList.addAll(friendsUser1);
		friendList.addAll(friendsUser2);

			for (Friend friend : friendList) {
				friendControl.delete(friend.getId());			
				}

			userControl.delete(user1.getId());
			userControl.delete(user2.getId());
		} catch (Exception e) {
		}
		var user1 = new User(0, "Johanes 1er du nom", "Johanespremierdunom@gmail.com", "ASafePassword@1235813", Role.citizen, LocalDateTime.now(),
            true);
		var user2 = new User(0, "Johanes 2eme du nom", "Johanesdeuxiemedunom@gmail.com", "ASafePassword@1235813", Role.citizen, LocalDateTime.now(),
            true);

		
		var user1WasCreated = userControl.create(user1).getBody() == user1;
		var user2WasCreated = userControl.create(user2).getBody() == user2;

		Assert.isTrue(user1WasCreated && user2WasCreated,"At least one of the two users has not been created");
	}

	}