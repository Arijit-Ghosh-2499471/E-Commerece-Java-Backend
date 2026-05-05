package com.cts.ecommerce;

import com.cts.ecommerce.entity.*;
import com.cts.ecommerce.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(EcommerceApplication.class);

	private final UserService userService;
	private final CartItemService cartItemService;
	private final CategoryService categoryService;
	private final OrderService orderService;
	private final ProductService productService;
	private final ShoppingCartService shoppingCartService;
	private final ReviewService reviewService;
	private final AddressService addressService;

	private User loggedInUser;

	public EcommerceApplication(
			UserService userService,
			CartItemService cartItemService,
			CategoryService categoryService,
			OrderService orderService,
			ProductService productService,
			ShoppingCartService shoppingCartService,
			ReviewService reviewService, AddressService addressService) {
		this.userService = userService;
		this.categoryService = categoryService;
		this.cartItemService = cartItemService;
		this.orderService = orderService;
		this.productService = productService;
		this.shoppingCartService = shoppingCartService;
		this.reviewService = reviewService;
		this.addressService = addressService;
	}

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Override
	public void run(String... args) {

		Scanner scanner = new Scanner(System.in);

		while (true) {
			log.info("""
					===========================
						E-COMMERCE PLATFORM
					===========================
					1. Register
					2. Login
					0. Exit
					Enter choice:
					""");

			int choice;
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				log.warn("Please Enter a Valid Number.");
				continue;
			}

			switch (choice) {
				case 1 -> register(scanner);
				case 2 -> login(scanner);
				case 0 -> {
					log.info("Exiting Application...");
					return;
				}
				default -> log.warn("Invalid Choice For Sign-In");
			}
		}
	}

	private void register(Scanner scanner) {
		log.info("Enter Name:");
		String name = scanner.nextLine();

		log.info("Enter Email:");
		String email = scanner.nextLine();

		log.info("Enter Password:");
		String password = scanner.nextLine();

		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		user.setRole("Customer");

		userService.register(user);
		log.info("Registration Successful. Please Login.");

	}

	private void login(Scanner scanner) {

		log.info("Enter Email:");
		String email = scanner.nextLine();

		log.info("Enter Password:");
		String password = scanner.nextLine();

		if (!userService.login(email, password)) {
			log.warn("Invalid Email or Password");
			return;
		}

		loggedInUser = userService.getUserByEmail(email);
		log.info("Welcome, {}", loggedInUser.getName());

		if ("Customer".equals(loggedInUser.getRole())) {
			userDashboard(scanner);
		} else {
			adminDashboard(scanner);
		}
	}

	private void adminDashboard(Scanner scanner) {

		while (loggedInUser != null) {
			log.info("""
                    ========================
                    	ADMIN DASHBOARD
                    ========================
                    1. View All Users
                    2. View All Products
                    3. Add Products
                    4. View Sales
                    0. Logout
                    Enter choice:
                    """);

			int choice;
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				log.warn("Not a Valid Number");
				continue;
			}

			switch (choice) {
				case 1 -> viewUsers();
				case 2 -> viewAllProducts();
				case 3 -> addNewProduct(scanner);
				case 4 -> viewSales();
				case 0 -> {
					loggedInUser = null;
					log.info("Logged Out Successfully");
				}
				default -> log.warn("Invalid Choice for Admin Dashboard");
			}
		}
	}

	private void viewUsers() {
		List<User> users = userService.getAllUsers();
		users.forEach(user ->
				log.info("ID: {} | Email: {} | Name: {} | Role: {}",
						user.getUserId(), user.getEmail(), user.getName(), user.getRole()));
	}

	private void viewAllProducts() {
		List<Product> products = productService.getAllProducts();
		products.forEach(product ->
				log.info("ID: {} | Name: {} | Description: {} | Price: {}",
						product.getProductId(), product.getProductName(), product.getDescription(), product.getPrice()));
	}

	private void addNewProduct(Scanner scanner) {
		log.info("Enter Product Name: ");
		String productName = scanner.nextLine();

		log.info("Enter Product Description: ");
		String productDescription = scanner.nextLine();

		log.info("Enter Product Price: ");
		double productPrice = Double.parseDouble(scanner.nextLine());

		List<Category> categories = categoryService.getAllCategories();

		if (categories.isEmpty()) {
			log.info("No Categories Available.");
			return;
		}

		log.info("Select Category:");
		for (Category category : categories) {
			System.out.println(
					category.getCategoryId() + ". " + category.getCategoryName()
			);
		}

		System.out.print("Enter Category ID: ");
		int categoryId = Integer.parseInt(scanner.nextLine());

		Category selectedCategory = categoryService.getCategoryById(categoryId);

		if (selectedCategory == null) {
			System.out.println("Invalid category selected.");
			return;
		}

		Product product = new Product();
		product.setProductName(productName);
		product.setDescription(productDescription);
		product.setPrice(productPrice);
		product.setCategoryId(selectedCategory.getCategoryId());

		productService.createProduct(product);

		System.out.println("Product added successfully.");
	}

	private void viewSales() {
		List<Order> orders = orderService.findAll();
		orders.forEach(order ->
				log.info("Order ID: {} | User ID: {} | Total Price: {} | Payment Status: {} | Order Status: {}",
						order.getOrderId(),  order.getUserId(), order.getTotalPrice(), order.getPaymentStatus(), order.getOrderStatus()));
	}





	private void userDashboard(Scanner scanner) {

		while (loggedInUser != null) {
			log.info("""
                    ======================
                    	USER DASHBOARD
                    ======================
                    1. Search for Products
                    2. View Products by Category
                    3. Order Products
                    4. View Shopping Cart
                    5. View My Orders
                    6. Give Review
                    7. See Product Reviews
                    8. See My Reviews
                    9. Add/Remove Address
                    10. Update Info
                    0. Logout
                    Enter choice:
                    """);

			int choice;
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				log.warn("Not a Valid Option");
				continue;
			}

			switch (choice) {
				case 1 -> searchProducts(scanner);
				case 2 -> viewProductsByCategory(scanner);
				case 3 -> addProductsToCart(scanner);
				case 4 -> viewShoppingCart(scanner);
				case 5 -> viewMyOrders();
				case 6 -> giveReview(scanner);
				case 7 -> viewProductReviews(scanner);
				case 8 -> viewMyReviews();
				case 9 -> modifyAddresses(scanner);
				case 10 -> updateInfo(scanner);
				case 0 -> {
					loggedInUser = null;
					log.info("Logged Out Successfully");
				}
				default -> log.warn("Invalid Choice for User Dashboard");
			}
		}
	}

	private void searchProducts(Scanner scanner) {
		log.info("Enter Product Name: ");
		String productName = scanner.nextLine();

		List<Product> products = productService.getProductsByName(productName);
		products.forEach(product ->
				log.info("ID: {} | Name: {} | Description: {} | Price: {} | Category: {}",
						product.getProductId(), product.getProductName(), product.getDescription(), product.getPrice(), categoryService.getCategoryById(product.getCategoryId()).getCategoryName()));
	}

	private void viewProductsByCategory(Scanner scanner) {
		List<Category> categories = categoryService.getAllCategories();
		log.info("Select Category:");
		for (Category category : categories) {
            log.info("{}. {} \n", category.getCategoryId(), category.getCategoryName());
		}
		log.info("Enter Category ID: ");
		int categoryId = Integer.parseInt(scanner.nextLine());
		Category selectedCategory = categoryService.getCategoryById(categoryId);
		if (selectedCategory == null) {
			System.out.println("Invalid category selected.");
			return;
		}
		List<Product> products = productService.getProductsByCategory(selectedCategory.getCategoryId());
		for (Product product : products) {
			log.info("Category: {} ID: {} | Name: {} | Description: {} | Price: {}",
					selectedCategory, product.getProductId(), product.getProductName(), product.getDescription(), product.getPrice());
		}
	}

	private void addProductsToCart(Scanner scanner) {
		log.info("Enter Product ID: ");
		int productId = Integer.parseInt(scanner.nextLine());

		productService.validateProductId(productId);

		log.info("Enter Quantity: ");
		int quantity = Integer.parseInt(scanner.nextLine());

		cartItemService.addItem(loggedInUser.getUserId(),  productId, quantity);
	}

	private void viewShoppingCart(Scanner scanner) {
		int userId = loggedInUser.getUserId();
		List<CartItem> cartItems = cartItemService.viewActiveCart(userId);

		if (cartItems.isEmpty()) {
			log.info("Your shopping cart is empty.");
			return;
		}

		log.info("=========== MY SHOPPING CART ===========");

		double totalPrice = 0.0;
		int shoppingCartId = 0;

		for (CartItem cartItem : cartItems) {
			Product product = productService.getProductById(cartItem.getProductId());
			double subTotal = product.getPrice() * cartItem.getQuantity();
			totalPrice += subTotal;
			shoppingCartId = cartItem.getShoppingCartId();

			log.info("ProductId: {} | Name: {} | Price: {} | Quantity: {} | Subtotal: {}",
					product.getProductId(), product.getProductName(), product.getPrice(), cartItem.getQuantity(), subTotal);
		}

		log.info("Total Cart Price: {}", totalPrice);
		log.info("1. Add Item");
		log.info("2. Remove Item");
		log.info("3. Checkout");
		log.info("0. Back");

		int choice = Integer.parseInt(scanner.nextLine());

		switch (choice) {
			case 1 -> {
				log.info("Enter Product ID: ");
				int productId = Integer.parseInt(scanner.nextLine());

				log.info("Enter Quantity: ");
				int quantity = Integer.parseInt(scanner.nextLine());

				cartItemService.addItem(shoppingCartId, productId, quantity);
				log.info("Item added to cart.");
			}

			case 2 -> {
				log.info("Enter Product ID: ");
				int productId = Integer.parseInt(scanner.nextLine());

				log.info("Enter Quantity: ");
				int quantity = Integer.parseInt(scanner.nextLine());

				cartItemService.removeItem(shoppingCartId, productId, quantity);
				log.info("Item removed from cart.");
			}

			case 3 -> {
				if(loggedInUser.getPaymentDetails() == null) {
					log.info("User payment details is empty.");
					return;
				}

				List<Address> addresses = addressService.getAddressesByUserId(userId);

				if (addresses.isEmpty()) {
					log.info("No address found. Please add an address before checkout.");
					return;
				}

				log.info("Select Shipping Address:");
				for (Address address : addresses) {
					log.info(
							"AddressId: {} | {}, {}, {}, {} - {}",
							address.getAddressId(),
							address.getHouseNo(),
							address.getArea(),
							address.getCity(),
							address.getState(),
							address.getPinCode()
					);

				}

				int selectedAddressId = Integer.parseInt(scanner.nextLine());

				Address selectedAddress = addresses.stream()
						.filter(a -> a.getAddressId() == selectedAddressId)
						.findFirst()
						.orElse(null);

				if (selectedAddress == null) {
					log.info("Invalid Address Selection.");
					return;
				}

				Order order = new Order();
				order.setUserId(userId);
				order.setShoppingCartId(shoppingCartId);
				order.setTotalPrice(totalPrice);
				order.setOrderStatus("Pending");
				order.setPaymentStatus("Paid");
				order.setShippingAddressId(selectedAddress.getAddressId());

				orderService.addOrder(order);
				shoppingCartService.checkout(userId);

				log.info("Order placed successfully.");
			}

            case 0 -> {
                return;
            }
            default -> log.info("Invalid option selected.");
		}
	}

	private void viewMyOrders() {
		int userId = loggedInUser.getUserId();
		List<Order> orders = orderService.findOrdersByUserId(userId);

		if (orders.isEmpty()) {
			log.info("No Orders Found For UserId = {}", userId);
			return;
		}

		log.info("=========== MY ORDERS ===========");

		for (Order order : orders) {
			log.info("Order ID       : {}", order.getOrderId());
			log.info("Order Status   : {}", order.getOrderStatus());
			log.info("Payment Status : {}", order.getPaymentStatus());
			log.info("Total Price    : {}", order.getTotalPrice());
			log.info("------------- ITEMS -------------");
			int shoppingCartId = order.getShoppingCartId();

			List<CartItem> cartItems = cartItemService.getItemsByShoppingCartId(shoppingCartId);

			for (CartItem cartItem : cartItems) {
				Product product = productService.getProductById(cartItem.getProductId());
				double subTotal = product.getPrice() * cartItem.getQuantity();

				log.info("Product: {} | Price: {} | Quantity: {} | Subtotal: {}",
						product.getProductName(), product.getPrice(), cartItem.getQuantity(), subTotal);
			}
			log.info("-----------------");
		}
	}

	private void giveReview(Scanner scanner) {
		log.info("============ GIVE REVIEW ===========");
		int userId = loggedInUser.getUserId();
		log.info("Enter Product Id to give review:");
		int productId = scanner.nextInt();

		log.info("Enter Rating (1-5) :");
		int rating = scanner.nextInt();

		log.info("Enter Description:");
		String description = scanner.next();

		Review review = new Review();
		review.setUserId(userId);
		review.setProductId(productId);
		review.setRating(rating);
		review.setReviewDescription(description);

		reviewService.createReview(review);
	}

	private void viewProductReviews(Scanner scanner) {
		log.info("Enter Product ID: ");
		int productId = Integer.parseInt(scanner.nextLine());

		productService.validateProductId(productId);

		List<Review> reviews = reviewService.getReviewByProductId(productId);
		for (Review review : reviews) {
			log.info("ReviewId: {} | User Name: {} | Rating: {} | Description: {}",
					review.getReviewId(),
					userService.getUserById(review.getUserId()).getName(),
					review.getRating(),
					review.getReviewDescription());
		}
	}

	private void viewMyReviews() {
		int userId = loggedInUser.getUserId();
		List<Review> reviews = reviewService.getReviewByUserId(userId);
		for (Review review : reviews) {
			log.info("ReviewId: {} | Product: {} | Rating: {} | Description: {}",
					review.getReviewId(),
					productService.getProductById(review.getProductId()).getProductName(),
					review.getRating(),
					review.getReviewDescription());
		}
	}

	private void modifyAddresses(Scanner scanner) {

		int userId = loggedInUser.getUserId();
		List<Address> addresses = addressService.getAddressesByUserId(userId);

		if (addresses.isEmpty()) {
			log.info("No addresses found for this user.");
		} else {
			log.info("======= YOUR ADDRESSES =======");
			for (Address address : addresses) {
				log.info(
						"AddressId: {} | {}, {}, {}, {}, {} - {}",
						address.getAddressId(),
						address.getHouseNo(),
						address.getArea(),
						address.getCity(),
						address.getState(),
						address.getCountry(),
						address.getPinCode()
				);
			}
		}

		log.info("""
            =====================
            1. Add Address
            2. Delete Address
            0. Back
            Enter choice:
            """);

		int choice;
		try {
			choice = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			log.info("Invalid choice.");
			return;
		}

		switch (choice) {
			case 1 -> {
				log.info("Enter House No:");
				String houseNo = scanner.nextLine();

				log.info("Enter Area:");
				String area = scanner.nextLine();

				log.info("Enter City:");
				String city = scanner.nextLine();

				log.info("Enter State:");
				String state = scanner.nextLine();

				log.info("Enter Country:");
				String country = scanner.nextLine();

				log.info("Enter Pin Code:");
				int pinCode = Integer.parseInt(scanner.nextLine());

				Address address = new Address(userId, houseNo, area, city, state, country, pinCode);

				addressService.addAddress(address);
				log.info("Address Added Successfully.");
			}

			case 2 -> {
				if (addresses.isEmpty()) {
					log.info("No Addresses Available To Delete.");
					return;
				}

				log.info("Enter Address ID to Delete:");
				int addressId = Integer.parseInt(scanner.nextLine());

				addressService.deleteAddress(addressId);
				log.info("Address Deleted Successfully.");
			}

			case 0 -> {
				return;
			}

			default -> log.info("Invalid Option Selected.");
		}
	}

	private void updateInfo(Scanner scanner) {
		int userId = loggedInUser.getUserId();
		log.info("""
            =====================
              UPDATE USER INFO
            =====================
            1. Update Name
            2. Update Password
            3. Set Payment Details
            0. Back
            Enter choice:
            """);

		int choice;
		try {
			choice = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			log.info("Invalid choice.");
			return;
		}

		switch (choice) {

			case 1 -> {
				log.info("Enter new name:");
				String newName = scanner.nextLine();

				loggedInUser.setName(newName);
				userService.updateUser(userId, loggedInUser);

				log.info("Name updated successfully.");
			}

			case 2 -> {
				log.info("Enter new password:");
				String newPassword = scanner.nextLine();

				loggedInUser.setPassword(newPassword);
				userService.updateUser(userId, loggedInUser);

				log.info("Password updated successfully.");
			}

			case 3 -> {
				log.info("""
            ===========================
              ADD PAYMENT METHOD
            ===========================
            1. Credit Card
            2. Debit Card
            3. UPI
            4. Net Banking
            Enter choice:
            """);

				int paymentChoice;
				try {
					paymentChoice = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					log.info("Invalid payment option.");
					return;
				}

				String paymentDetails;

				switch (paymentChoice) {
					case 1 -> paymentDetails = "Credit Card";
					case 2 -> paymentDetails = "Debit Card";
					case 3 -> paymentDetails = "UPI";
					case 4 -> paymentDetails = "Net Banking";
					default -> {
						log.info("Invalid payment option selected.");
						return;
					}
				}

				loggedInUser.setPaymentDetails(paymentDetails);
				userService.updateUser(loggedInUser.getUserId(), loggedInUser);

				log.info("Payment method updated to: {}", paymentDetails);
			}
			case 0 -> {
				return;
			}

			default -> log.info("Invalid option selected.");
		}
	}

}