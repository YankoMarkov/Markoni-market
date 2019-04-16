package org.yanmark.markoni.utils;

import org.yanmark.markoni.domain.entities.Category;
import org.yanmark.markoni.domain.entities.Comment;
import org.yanmark.markoni.domain.entities.Product;
import org.yanmark.markoni.domain.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {

    public static Category getTestCategory() {
        Category testCategory = new Category();
        testCategory.setId("1qaz2wsx");
        testCategory.setName("testName");
        return testCategory;
    }

    public static List<Category> getTestCategories(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new Category() {{
                    setId(index + "x");
                    setName("model " + index);
                }})
                .collect(Collectors.toList());
    }

    public static Product getTestProduct() {
        Product testProduct = new Product();
        testProduct.setId("1qaz2wsx");
        testProduct.setName("testName");
        testProduct.setImage("testImage");
        testProduct.setWeight(1.5);
        testProduct.setDescription("testDescription");
        testProduct.setPrice(BigDecimal.ONE);
        testProduct.setRating(0);
        testProduct.setQuantity(1);
        testProduct.setComments(new HashSet<>());
        testProduct.setCategories(new HashSet<>());
        return testProduct;
    }

    public static List<Product> getTestProducts(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new Product() {{
                    setId(index + "x");
                    setName("model " + index);
                    setImage("image " + index);
                    setWeight(index + 1.5);
                    setDescription("description " + index);
                    setPrice(BigDecimal.valueOf(index));
                    setRating(0);
                    setQuantity(index + 1);
                    setComments(new HashSet<>());
                    setCategories(new HashSet<>());
                }})
                .collect(Collectors.toList());
    }

    public static Comment getTestComment() {
        return new Comment() {{
            setId("1qaz2wsx");
            setProduct(new Product() {{
                setId("1qaz2wsx");
                setName("testName");
                setImage("testImage");
                setWeight(1.5);
                setDescription("testDescription");
                setPrice(BigDecimal.ONE);
                setRating(0);
                setQuantity(1);
                setComments(new HashSet<>());
                setCategories(new HashSet<>());
            }});
            setUser(new User() {{
                setId("1qaz2wsx");
                setUsername("testUser");
                setPassword("1qaz2wsx");
                setEmail("t@t.t");
                setAddress("testAddress");
            }});
            setComment("testComment");
            setTime(LocalDateTime.now());
        }};
    }

    public static List<Comment> getTestComments(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new Comment() {{
                    setId(index + "x");
                    setProduct(new Product() {{
                        setId("1qaz2wsx");
                        setName("testName");
                        setImage("testImage");
                        setWeight(1.5);
                        setDescription("testDescription");
                        setPrice(BigDecimal.ONE);
                        setRating(0);
                        setQuantity(1);
                        setComments(new HashSet<>());
                        setCategories(new HashSet<>());
                    }});
                    setUser(new User() {{
                        setId("1qaz2wsx");
                        setUsername("testUser");
                        setPassword("1qaz2wsx");
                        setEmail("t@t.t");
                        setAddress("testAddress");
                    }});
                    setComment("comment " + index);
                    setTime(LocalDateTime.now());
                }})
                .collect(Collectors.toList());
    }
}
