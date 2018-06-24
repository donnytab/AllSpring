package com.donnytab.AllSpring;

import org.apache.geode.cache.client.ClientRegionShortcut;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling

//@ClientCacheApplication(name = "AccessingDataGemFireApplication", logLevel = "error")
//@EnableEntityDefinedRegions(basePackageClasses = Person.class,
//        clientRegionShortcut = ClientRegionShortcut.LOCAL)
//@EnableGemfireRepositories

//public class AllSpringApplication {
public class AllSpringApplication implements CommandLineRunner {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AllSpringApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(AllSpringApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

//    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//        return args -> {
//            Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
//            log.info(quote.toString());
//        };
//    }

    public void run(String... strings) throws Exception {
        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        List<Object[]> splitUpNames = Arrays.asList("John Wood", "Nelson Craw").stream()
                .map(name->name.split(" "))
                .collect(Collectors.toList());

        splitUpNames.forEach(name->log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        log.info("Querying for customer records where first_name = 'John':");

        jdbcTemplate.query("SELECT id, first_name, last_name FROM customers WHERE first_name=?", new Object[]{"John"},
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
    }


    /* Accessing Data in Pivotal GemFire

    @Bean
    ApplicationRunner run(PersonRepository personRepository) {

        return args -> {

            Person alice = new Person("Alice", 40);
            Person bob = new Person("Baby Bob", 1);
            Person carol = new Person("Teen Carol", 13);

            System.out.println("Before accessing data in Pivotal GemFire...");

            asList(alice, bob, carol).forEach(person -> System.out.println("\t" + person));

            System.out.println("Saving Alice, Bob and Carol to Pivotal GemFire...");

            personRepository.save(alice);
            personRepository.save(bob);
            personRepository.save(carol);

            System.out.println("Lookup each person by name...");

            asList(alice.getName(), bob.getName(), carol.getName())
              .forEach(name -> System.out.println("\t" + personRepository.findByName(name)));

            System.out.println("Query adults (over 18):");

            stream(personRepository.findByAgeGreaterThan(18).spliterator(), false)
              .forEach(person -> System.out.println("\t" + person));

            System.out.println("Query babies (less than 5):");

            stream(personRepository.findByAgeLessThan(5).spliterator(), false)
              .forEach(person -> System.out.println("\t" + person));

            System.out.println("Query teens (between 12 and 20):");

            stream(personRepository.findByAgeGreaterThanAndAgeLessThan(12, 20).spliterator(), false)
              .forEach(person -> System.out.println("\t" + person));
        };
    }*/
}
