package com.cts.ecommerce;

import com.cts.ecommerce.entity.Address;
import com.cts.ecommerce.entity.Order;
import com.cts.ecommerce.service.AddressService;
import com.cts.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
public class EcommerceApplication {

	private static OrderService orderService;
	private static AddressService addressService;

	@Autowired
	public EcommerceApplication(OrderService orderService, AddressService addressService) {
		EcommerceApplication.orderService = orderService;
		EcommerceApplication.addressService = addressService;
	}


    public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
		System.out.println("Hello world");
		Scanner sc = new Scanner(System.in);
		while(true){
			System.out.println("Select an option :\n 1.Get all orders\n 2.Get orders by order ID\n 3.Get orders by user ID\n 4.Update the payment status\n 5.Update the order status\n 6.Place an order\n 7.Exit");
			int option = sc.nextInt();
			switch (option){
				case 1:{
					System.out.println("\nThe orders placed are: ");
					orderService.findAll().forEach(System.out::println);
				}
				break;
				case 2 : {
					System.out.println("\nEnter the order ID: ");
					int oId = sc.nextInt();
					System.out.println(orderService.findById(oId));
				}
				break;
				case 3 : {
					System.out.println("\nEnter the user ID: ");
					int uId = sc.nextInt();
					System.out.println(orderService.findOrdersByUserId(uId));
				}
				break;
				case 5 : {
					System.out.println("\nEnter the order ID : ");
					int oId = sc.nextInt();
					sc.nextLine();
					System.out.println("\nEnter the order status");
					String st = sc.nextLine();
					if(orderService.updateOrderStatus(oId,st) > 0){
						System.out.println("Status Updated to : "+st+" for the order ID-"+oId);
					}
				}
				break;
				case 4 : {
					System.out.println("\nEnter the order ID : ");
					int oId = sc.nextInt();
					sc.nextLine();
					System.out.println("\nEnter the payment status");
					String pt = sc.nextLine();
					if(orderService.processPayment(oId,pt) > 0){
						System.out.println("Status Updated to : "+pt+" for the order ID-"+oId);
					}
				}
				break;
				case 6 :{
					System.out.println("Enter the user ID : ");
					int userId = sc.nextInt();
					sc.nextLine();

					System.out.println("\nProducts in your cart:");
					orderService.getCartProducts(userId).forEach(System.out::println);

					double totalPrice = orderService.calculateTotalPrice(userId);
					System.out.println("Total Price: " + totalPrice);

					System.out.println("\nEnter Shipping Address details:");
					System.out.print("House No: ");
					String houseNo = sc.nextLine();
					System.out.print("Area: ");
					String area = sc.nextLine();
					System.out.print("City: ");
					String city = sc.nextLine();
					System.out.print("State: ");
					String state = sc.nextLine();
					System.out.print("Country: ");
					String country = sc.nextLine();
					System.out.print("Pincode: ");
					int pincode = sc.nextInt();
					sc.nextLine();

					Address address = new Address(userId, houseNo, area, city, state, country, pincode);
					if (addressService.addAddress(address) > 0) {
						System.out.println("Address added successfully!");
					}

					int shippingAddressId = addressService.getIdOfAddress(userId);
					String orderStatus = "Pending";
					String paymentStatus = "Pending";
					int shoppingCartId = orderService.getShoppingCartId(userId);

					if (orderService.addOrder(new Order(userId, totalPrice, shippingAddressId, orderStatus, paymentStatus, shoppingCartId)) > 0) {
						System.out.println("Order placed successfully");
					} else {
						System.out.println("Error in placing order");
					}
				}
				break;
				case 7 : {
					return;
				}
				default: System.out.println("Enter the correct option");
				break;
			}
		}
	}

}
