# Shutdown a Spring Boot app when the Hashicorp Vault `max_ttl` of the database credentials expire

This code example is used in my blog post 
[Hashicorp Vault max_ttl Killed My Spring App](https://secrets-as-a-service.com/posts/hashicorp-vault/spring-boot-max_ttl/).

It ensures that the Spring Boot application is shutdown, whenever the dynamic database credentials expire. When the 
application is automatically restarted by e.g. Kubernetes or systemd this can solve the problem, that the application 
cannot access the database anymore, whenever the Vault lease of its database secrets expire.