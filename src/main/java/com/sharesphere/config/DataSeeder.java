package com.sharesphere.config;

import com.sharesphere.entity.*;
import com.sharesphere.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) { log.info("Data already seeded, skipping."); return; }
        log.info("Seeding initial data...");

        // Categories
        List<Category> categories = List.of(
            cat("Electronics","💻","Phones, laptops, cameras, gadgets"),
            cat("Books","📚","Textbooks, novels, reference books"),
            cat("Calculators","🧮","Scientific and graphing calculators"),
            cat("Engineering Tools","📐","Drawing sets, instruments, tools"),
            cat("Lab Equipment","🔬","Microscopes, beakers, lab instruments"),
            cat("Sports Equipment","⚽","Balls, rackets, gym equipment"),
            cat("Furniture","🪑","Tables, chairs, shelves, lamps"),
            cat("Cycles","🚲","Bicycles and accessories"),
            cat("Bags","🎒","Backpacks, laptop bags, luggage"),
            cat("Musical Instruments","🎸","Guitars, keyboards, drums"),
            cat("Event Equipment","🎤","Projectors, mics, displays"),
            cat("Hostel Items","🏠","Kettles, buckets, fans, utensils"),
            cat("Study Materials","📝","Notes, question papers, assignments"),
            cat("Other","📦","Everything else")
        );
        categoryRepository.saveAll(categories);

        // Users
        User admin = userRepository.save(User.builder()
            .name("Admin").email("admin@sharesphere.com")
            .password(passwordEncoder.encode("Admin@123"))
            .role(Role.ADMIN).college("ShareSphere HQ").location("Campus").build());

        User rahul = userRepository.save(User.builder()
            .name("Rahul Sharma").email("rahul@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .phone("9876543210").college("IIT Campus").location("Hostel A").build());

        User priya = userRepository.save(User.builder()
            .name("Priya Verma").email("priya@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .phone("9123456789").college("IIT Campus").location("Hostel B").build());

        User arjun = userRepository.save(User.builder()
            .name("Arjun Mehta").email("arjun@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .college("IIT Campus").location("Block C").build());

        User sneha = userRepository.save(User.builder()
            .name("Sneha Patel").email("sneha@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .college("IIT Campus").location("Library Block").build());

        User dev = userRepository.save(User.builder()
            .name("Dev Kumar").email("dev@college.edu")
            .password(passwordEncoder.encode("Student@123"))
            .college("IIT Campus").location("Hostel C").build());

        // Re-fetch saved categories from DB to get real IDs
        List<Category> savedCats = categoryRepository.findAll();
        Category electronics = savedCats.stream().filter(c->c.getName().equals("Electronics")).findFirst().orElse(savedCats.get(0));
        Category books = savedCats.stream().filter(c->c.getName().equals("Books")).findFirst().orElse(savedCats.get(1));
        Category calculators = savedCats.stream().filter(c->c.getName().equals("Calculators")).findFirst().orElse(savedCats.get(2));
        Category engTools = savedCats.stream().filter(c->c.getName().equals("Engineering Tools")).findFirst().orElse(savedCats.get(3));
        Category sports = savedCats.stream().filter(c->c.getName().equals("Sports Equipment")).findFirst().orElse(savedCats.get(5));
        Category cycles = savedCats.stream().filter(c->c.getName().equals("Cycles")).findFirst().orElse(savedCats.get(7));
        Category hostel = savedCats.stream().filter(c->c.getName().equals("Hostel Items")).findFirst().orElse(savedCats.get(11));
        Category other = savedCats.stream().filter(c->c.getName().equals("Other")).findFirst().orElse(savedCats.get(13));
        Category furniture = savedCats.stream().filter(c->c.getName().equals("Furniture")).findFirst().orElse(savedCats.get(6));

        // Items
        List<Item> items = List.of(
            Item.builder().name("Casio FX-991CW Scientific Calculator")
                .description("Latest Casio FX-991CW, 552 functions, natural display. Perfect for semester exams.")
                .category(calculators).condition("Like New").price(new BigDecimal("900"))
                .rentalPricePerDay(new BigDecimal("30")).securityDeposit(new BigDecimal("300"))
                .listingType(ListingType.SELL_AND_RENT).owner(rahul).location("Hostel A").build(),

            Item.builder().name("DSLR Camera Canon 200D")
                .description("Canon 200D with 18-55mm kit lens, 24.1MP. Great for photography projects and events.")
                .category(electronics).condition("Good").price(new BigDecimal("28000"))
                .rentalPricePerDay(new BigDecimal("300")).securityDeposit(new BigDecimal("2000"))
                .listingType(ListingType.SELL_AND_RENT).owner(rahul).location("Hostel A").build(),

            Item.builder().name("Arduino Uno Starter Kit")
                .description("Arduino Uno R3 with 37-sensor kit, breadboard, jumper wires. Ideal for IoT projects.")
                .category(electronics).condition("New").price(new BigDecimal("1500"))
                .rentalPricePerDay(new BigDecimal("50")).securityDeposit(new BigDecimal("500"))
                .listingType(ListingType.SELL_AND_RENT).owner(priya).location("Lab Block").build(),

            Item.builder().name("Engineering Drawing Kit")
                .description("Staedtler 551 drawing set with compass, mini-drafter, set squares. First year essential.")
                .category(engTools).condition("Good").price(new BigDecimal("800"))
                .rentalPricePerDay(new BigDecimal("20")).securityDeposit(new BigDecimal("200"))
                .listingType(ListingType.SELL_AND_RENT).owner(arjun).location("Block C").build(),

            Item.builder().name("Mountain Bicycle")
                .description("Hero Sprint 21-speed MTB, well maintained. Perfect for campus commute.")
                .category(cycles).condition("Good").price(new BigDecimal("6000"))
                .rentalPricePerDay(new BigDecimal("100")).securityDeposit(new BigDecimal("1000"))
                .listingType(ListingType.SELL_AND_RENT).owner(arjun).location("Parking Lot B").build(),

            Item.builder().name("Data Structures & Algorithms - CLRS 4th Ed")
                .description("Introduction to Algorithms by CLRS. Excellent condition, no markings.")
                .category(books).condition("Like New").price(new BigDecimal("1200"))
                .listingType(ListingType.SELL).owner(sneha).location("Library Block").build(),

            Item.builder().name("Laptop Stand Adjustable")
                .description("Aluminum adjustable laptop stand, foldable. Reduces neck strain during long study sessions.")
                .category(other).condition("New")
                .price(new BigDecimal("1400")).listingType(ListingType.SELL)
                .owner(sneha).location("Library Block").build(),

            Item.builder().name("Football (Nivia)")
                .description("Nivia Storm Football, Size 5. Lightly used, great for hostel grounds.")
                .category(sports).condition("Good").price(new BigDecimal("600"))
                .rentalPricePerDay(new BigDecimal("20")).securityDeposit(new BigDecimal("100"))
                .listingType(ListingType.SELL_AND_RENT).owner(dev).location("Hostel C").build(),

            Item.builder().name("Hostel Electric Kettle 1L")
                .description("Prestige 1-litre stainless steel kettle. Perfect for hostel rooms.")
                .category(hostel).condition("Good").price(new BigDecimal("500"))
                .listingType(ListingType.SELL).owner(dev).location("Hostel C").build(),

            Item.builder().name("Raspberry Pi 4 Model B (4GB)")
                .description("Raspberry Pi 4B 4GB RAM, unused. Comes with SD card and case.")
                .category(electronics).condition("New").price(new BigDecimal("5500"))
                .rentalPricePerDay(new BigDecimal("150")).securityDeposit(new BigDecimal("1000"))
                .listingType(ListingType.SELL_AND_RENT).owner(priya).location("Lab Block").build(),

            Item.builder().name("Badminton Racket Set (2 pcs)")
                .description("Yonex Astrox pair with 3 shuttlecocks. Good for hostel court matches.")
                .category(sports).condition("Good").price(new BigDecimal("1400"))
                .rentalPricePerDay(new BigDecimal("40")).securityDeposit(new BigDecimal("200"))
                .listingType(ListingType.SELL_AND_RENT).owner(rahul).location("Sports Block").build(),

            Item.builder().name("Operating Systems - Galvin 10th Ed")
                .description("OS concepts by Silberschatz. Must-have for CS students. Minor pencil markings.")
                .category(books).condition("Fair").price(new BigDecimal("750"))
                .listingType(ListingType.SELL).owner(arjun).location("Block C").build(),

            Item.builder().name("Wireless Bluetooth Speaker")
                .description("JBL Go 3, waterproof, 5hr battery. For hostel room or outdoor trips.")
                .category(electronics).condition("Like New").price(new BigDecimal("2500"))
                .rentalPricePerDay(new BigDecimal("70")).securityDeposit(new BigDecimal("500"))
                .listingType(ListingType.SELL_AND_RENT).owner(sneha).location("Hostel B").build(),

            Item.builder().name("Study Table with Drawer")
                .description("Wooden study table, 120x60cm with 2 drawers. Pickup from Hostel B room 204.")
                .category(furniture)
                .condition("Good").price(new BigDecimal("2500"))
                .listingType(ListingType.SELL).owner(priya).location("Hostel B").build(),

            Item.builder().name("Crimping Tool + RJ45 Connectors Kit")
                .description("Network crimping tool with 100 RJ45 connectors. For networking lab projects.")
                .category(engTools).condition("New").price(new BigDecimal("450"))
                .rentalPricePerDay(new BigDecimal("15")).securityDeposit(new BigDecimal("100"))
                .listingType(ListingType.SELL_AND_RENT).owner(dev).location("Hostel C").build()
        );
        itemRepository.saveAll(items);
        log.info("Seeded {} categories, {} users, {} items", categories.size(), 6, items.size());
    }

    private Category cat(String name, String icon, String desc) {
        return Category.builder().name(name).icon(icon).description(desc).build();
    }
}
