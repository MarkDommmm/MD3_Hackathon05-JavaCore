package RA.Run;

import RA.Config.InputMethods;
import RA.Controller.CatalogController;
import RA.Controller.ProductController;
import RA.Model.Catalog;
import RA.Model.Product;


import java.util.*;

public class BookManagement {
    static ProductController productController = new ProductController();
    static CatalogController catalogController = new CatalogController();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("**************************BASIC-MENU**************************\n" +
                    "1. Quản lý danh mục [5 điểm]\n" +
                    "2. Quản lý sản phẩm [5 điểm]\n" +
                    "3. Thoát");
            System.out.println("Mời bạn chọn lựa từ 1 -3 cảm ơn!!!");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    catalogManagement();
                    break;
                case 2:
                    productManagement();
                    break;
                case 3:

                    break;
                default:
                    System.out.println("Xin vui lòng nhập lại nhé !!!");
            }
        }
    }

    public static void catalogManagement() {
        System.out.println("********************CATALOG-MANAGEMENT********************\n" +
                "1. Nhập số danh mục thêm mới và nhập thông tin cho từng danh mục [5 điểm]\n" +
                "2. Hiển thị thông tin tất cả các danh mục [5 điểm]\n" +
                "3. Sửa tên danh mục theo mã danh mục [5 điểm]\n" +
                "4. Xóa danh muc theo mã danh mục (lưu ý ko xóa khi có sản phẩm) [5 điểm]\n" +
                "5. Quay lại\n");
        System.out.println("Xin vui lòng nhập từ 1 - 5 xin cảm ơn!!!");
        byte choice = InputMethods.getByte();
        switch (choice) {
            case 1:
                addCatalog();
                break;
            case 2:
                showCatalog();
                break;
            case 3:
                updateCatalog();
                break;
            case 4:
                deleteCatalog();
                break;
            case 5:
                System.out.println();
                break;
            default:
                System.out.println("Xin vui lòng nhập lại từ 1 - 5");
        }
    }

    public static void addCatalog() {
        System.out.println("Xin vui lòng nhập số lượng tác giả muốn thêm");
        int n = InputMethods.getInteger();
        for (int i = 0; i < n; i++) {
            System.out.println("Nhập thông tin danh mục " + (i + 1));
            Catalog newCatalog = new Catalog();
            int newID = catalogController.getNewId();
            newCatalog.setCatalogId(newID);
            System.out.println("New ID: " + newID);
            newCatalog.inputData();
            catalogController.save(newCatalog);
            System.out.println("Danh mục đã được thêm mới thành công.");
        }
    }

    public static void showCatalog() {
        if (catalogController.getSize() == 0) {
            System.out.println("Không có thư mục  nào hết!!!");
            return;
        }
        for (Catalog catalog : catalogController.getCatalogList()) {
            catalog.displayData();
        }
    }

    public static void updateCatalog() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập mã danh mục cần sửa: ");
        int catalogId = scanner.nextInt();
        scanner.nextLine();

        Catalog catalog = catalogController.getById(catalogId);
        if (catalog != null) {
            System.out.print("Nhập tên danh mục mới: ");
            String newCatalogName = scanner.nextLine();
            catalog.setCatalogName(newCatalogName);
            catalogController.save(catalog);
            System.out.println("Sửa tên danh mục thành công.");
        } else {
            System.out.println("Không tìm thấy danh mục với mã danh mục đã nhập.");
        }
    }

    public static void deleteCatalog() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập mã danh mục cần xóa: ");
        int catalogId = scanner.nextInt();
        scanner.nextLine();
        if (productController.getProductAll().isEmpty()) {
            catalogController.delete(catalogId);
        } else {
            System.out.println("Không được xóa khi có sản phẩm.");
        }
    }


    //    ==================================================================
    public static void productManagement() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("********************PRODUCT-MANAGEMENT********************\n" +
                "1. Nhập số sản sản phẩm và nhập thông tin sản phẩm [5 điểm]\n" +
                "2. Hiển thị thông tin các sản phẩm [5 điểm]\n" +
                "3. Sắp xếp sản phẩm theo giá giảm dần [5 điểm]\n" +
                "4. Xóa sản phẩm theo mã [5 điểm]\n" +
                "5. Tìm kiếm sách theo tên sách [10 điểm]\n" +
                "6. Thay đổi thông tin của sách theo mã sách [10 điểm]\n" +
                "7. Quay lại");
        System.out.println("Xin mời lựa chọn từ 1  - 7");
        byte choice = InputMethods.getByte();
        switch (choice) {
            case 1:
                addProduct();
                break;
            case 2:
                showProduct();
                break;
            case 3:
                Collections.sort(productController.getProductAll(), Comparator.comparingDouble(Product::getProductPrice).reversed());
                break;
            case 4:
                deleteProduct();
                break;
            case 5:
                searchProductByName();
                break;
            case 6:
                updateProduct();
                break;
            case 7:
                System.out.println();
                break;
            default:
                System.out.println("Xin vui lòng nhập lại từ 1 - 7");
        }
    }

     public static void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập số sản phẩm mới: ");
        int numProducts = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < numProducts; i++) {
            System.out.println("Nhập thông tin cho sản phẩm thứ " + (i + 1));
            Product newProduct = new Product();
            newProduct.inputData();
            showCatalog();
            Catalog newCatalog = null;
            System.out.println("Chọn ID danh mục cần thêm: ");
            boolean flag = true;
            while (flag) {
                int idCatalog = InputMethods.getInteger();
                for (Catalog catalog : catalogController.getCatalogList()) {
                    if (catalog.getCatalogId() == idCatalog) {
                        newCatalog = catalog;
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    System.out.println("ID không tồn tại. Vui lòng nhập lại: ");
                }
            }
            newProduct.setCatalog(newCatalog);
            productController.save(newProduct);
        }
        System.out.println("Danh mục đã được thêm mới thành công.");
        showProduct();
    }

    public static void showProduct() {
        if (productController.getSize() == 0) {
            System.out.println("Không có thư mục  nào hết!!!");
            return;
        }
        for (Product product : productController.getProductAll()) {
            product.displayData();
        }
    }

    public static void deleteProduct() {
        Scanner scanner = new Scanner(System.in);
        showProduct();
        System.out.println("Nhập ID sản phẩm cần xóa");
        String id = scanner.nextLine();
        productController.delete(id);
    }

    public static Product searchProductByName() {
        List<Product> listSearch = new ArrayList<>();
        boolean check = false;
        System.out.println("Nhập tên sản phẩm muốn tìm kiếm: ");
        String name = InputMethods.getString();
        for (Product product : productController.getProductAll()) {
            if (product.getProductName().toLowerCase().contains(name.toLowerCase())) {
                product.displayData();
                break;
            }
        }
        System.out.println("Không tim thấy sản phẩm theo tác giả");
        return null;
    }

    public static void updateProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập mã sản phẩm cần thay đổi thông tin: ");
        String productId = scanner.nextLine();

        Product product = productController.findById(productId);
        if (product != null) {
            System.out.println("Nhập thông tin mới cho sản phẩm:");
            System.out.print("Tên sản phẩm: ");
            String newProductName = scanner.nextLine();

            System.out.print("Giá sản phẩm: ");
            double newProductPrice = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Mô tả sản phẩm: ");
            String newDescription = scanner.nextLine();

            System.out.print("Số lượng tồn kho: ");
            int newStock = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Trạng thái sản phẩm (true: bán, false: không bán): ");
            boolean newStatus = scanner.nextBoolean();
            scanner.nextLine();
            product.setProductName(newProductName);
            product.setProductPrice(newProductPrice);
            product.setDescription(newDescription);
            product.setStock(newStock);
            product.setStatus(newStatus);

            productController.save(product);
            System.out.println("Thay đổi thông tin sản phẩm thành công.");
        } else {
            System.out.println("Không tìm thấy sản phẩm với mã sản phẩm đã nhập.");
        }
    }
}
