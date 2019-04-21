package org.yanmark.markoni.utils;

import org.yanmark.markoni.domain.entities.Package;
import org.yanmark.markoni.domain.entities.*;
import org.yanmark.markoni.domain.models.bindings.categories.CategoryCreateBindingModel;
import org.yanmark.markoni.domain.models.bindings.comments.CommentEditBindingModel;
import org.yanmark.markoni.domain.models.bindings.users.UserEditBindingModel;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public static CategoryCreateBindingModel getTestCategoryBindingModel() {
        return new CategoryCreateBindingModel() {{
            setName("testName");
        }};
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
        testProduct.setQuantity(10);
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
                    setQuantity(10);
                    setComments(new HashSet<>());
                    setCategories(new HashSet<>());
                }})
                .collect(Collectors.toList());
    }

    public static OrderProduct getTestOrderProduct() {
        return new OrderProduct() {{
            setId("1qaz2wsx");
            setName("testName");
            setImage("imageTest");
            setWeight(1.5);
            setDescription("descriptionTest");
            setPrice(BigDecimal.ONE);
            setQuantity(1);
        }};
    }

    public static Order getTestOrder() {
        return new Order() {{
            setId("1qaz2wsx");
            setPrice(BigDecimal.ONE);
            setCustomer(new User());
            setProduct(new Product());
            setOrderedOn(LocalDate.now());
            setQuantity(1);
        }};
    }

    public static List<Order> getTestOrders(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new Order() {{
                    setId(index + "x");
                    setQuantity(index);
                    setOrderedOn(LocalDate.now());
                    setCustomer(new User());
                    setProduct(new Product());
                    setPrice(BigDecimal.ONE);
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

    public static CommentEditBindingModel getTestEditComment() {
        return new CommentEditBindingModel() {{
            setId("1qaz2wsx");
            setUser("testUser");
            setTime("10-10-2019 22:31:00");
            setComment("testComment");
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

    public static User getTestUser() {
        return new User() {{
            setId("1qaz2wsx");
            setUsername("testUser");
            setPassword("123");
            setEmail("t@t.t");
            setAddress("testAddress");
            setPackages(new HashSet<>());
            setReceipts(new HashSet<>());
            setComments(new HashSet<>());
            setAuthorities(new HashSet<>());
        }};
    }

    public static List<User> getTestUsers(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new User() {{
                    setId(index + "x");
                    setUsername("testUser" + index);
                    setPassword("123");
                    setEmail("t" + index + "@t.t");
                    setAddress("testAddress");
                    setPackages(new HashSet<>());
                    setReceipts(new HashSet<>());
                    setComments(new HashSet<>());
                    setAuthorities(new HashSet<>());
                }})
                .collect(Collectors.toList());
    }

    public static UserEditBindingModel getTestEditUser() {
        return new UserEditBindingModel() {{
            setUsername("testUser");
            setPassword("123");
            setNewPassword("1234");
            setConfirmNewPassword("1234");
            setEmail("t@t.t");
        }};
    }

    public static Receipt getTestReceipt() {
        return new Receipt() {{
            setId("1qaz2wsx");
            setFee(BigDecimal.ONE);
            setIssuedOn(LocalDateTime.now());
            setPakage(new Package());
            setRecipient(new User());
        }};
    }

    public static List<Receipt> getTestReceipts(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new Receipt() {{
                    setId(index + "x");
                    setFee(BigDecimal.valueOf(index));
                    setIssuedOn(LocalDateTime.now());
                    setRecipient(new User());
                    setPakage(new Package());
                }})
                .collect(Collectors.toList());
    }

    public static Package getTestPackage() {
        return new Package() {{
            setId("1qaz2wsx");
            setDescription("testDescription");
            setEstimatedDeliveryDay(LocalDateTime.now());
            setRecipient(new User());
            setShippingAddress("TestAddress");
            setWeight(1.5);
            setStatus(Status.ACQUIRED);
        }};
    }

    public static List<Package> getTestPackages(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new Package() {{
                    setId(index + "x");
                    setDescription("testDescription");
                    setEstimatedDeliveryDay(LocalDateTime.now());
                    setRecipient(new User());
                    setShippingAddress("TestAddress");
                    setWeight(1.5 + index);
                    setStatus(Status.ACQUIRED);
                }})
                .collect(Collectors.toList());
    }

    public static UserRole getTestUserRole() {
        return new UserRole() {{
            setId("1qaz2wsx");
            setAuthority("USER");
        }};
    }

    public static List<UserRole> getTestUserRoles(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new UserRole() {{
                    setId(index + "x");
                    setAuthority("USER");
                }})
                .collect(Collectors.toList());
    }
}
