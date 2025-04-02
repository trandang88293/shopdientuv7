 {
        "orderId": 1,
        "orderDate": "2025-03-26T10:00:00",
        "totalAmount": 500.0,
        "status": "Pending",
        "account": {
            "accountId": 1,
            "avatar": "avatar1.png",
            "name": "User One",
            "username": "user1",
            "password": "password123",
            "email": "user1@example.com",
            "phone": "0123456789",
            "role": 1,
            "isActive": true
        },
        "coupon": {
            "couponId": 1,
            "code": "DISCOUNT10",
            "discountPercentage": 10,
            "maxDiscount": 50.0,
            "minOrderValue": 100.0,
            "startDate": "2025-03-01T00:00:00",
            "endDate": "2025-04-01T23:59:59",
            "quantity": 5
        },
        "address": {
            "addressId": 1,
            "recipientName": "User One",
            "phoneNumber": "0123456789",
            "provinceId": "P01",
            "districtId": "D01",
            "wardId": "W01",
            "thirdPartyField": "Additional Info",
            "account": {
                "accountId": 1,
                "avatar": "avatar1.png",
                "name": "User One",
                "username": "user1",
                "password": "password123",
                "email": "user1@example.com",
                "phone": "0123456789",
                "role": 1,
                "isActive": true
            }
        },
        "orderDetails": [
            {
                "orderDetailId": 1,
                "quantity": 1,
                "price": 500.0,
                "productAttribute": {
                    "id": 1,
                    "sku": "PHX-001",
                    "price": 500.0,
                    "stockQuantity": 10,
                    "imageUrl": "phone.jpg",
                    "product": {
                        "productId": 1,
                        "name": "Smartphone X",
                        "description": "Smartphone with 8GB RAM",
                        "price": 500.0,
                        "isActive": true,
                        "category": {
                            "categoryId": 1,
                            "categoryName": "Electronics"
                        }
                    },
                    "productAttributeValues": []
                }
            }
        ]
    },