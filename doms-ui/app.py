import streamlit as st
import requests
import pandas as pd
import uuid

ORDER_SERVICE = "http://localhost:8081"

st.set_page_config(page_title="Distributed Order System", layout="wide")

st.title("🛒 Distributed Order Management System")

# creating order
st.header("Create Order")

user_id = st.text_input("User ID", "11111111-1111-1111-1111-111111111111")

col1, col2 = st.columns(2)

with col1:
    product_a = st.number_input("Product A Quantity", 0, 10, 2)
with col2:
    product_b = st.number_input("Product B Quantity", 0, 10, 1)

amount = st.number_input("Total Amount", 100, 10000, 500)

if st.button("Place Order"):
    payload = {
        "userId": user_id,
        "amount": amount,
        "items": {
            "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa": product_a,
            "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb": product_b
        }
    }

    r = requests.post(f"{ORDER_SERVICE}/orders", json=payload)

    if r.status_code == 200:
        st.success("Order placed successfully!")
    else:
        st.error("Order failed")

# orders table
st.header("Orders")

if st.button("Refresh Orders"):
    r = requests.get(f"{ORDER_SERVICE}/orders?userId={user_id}&page=0&size=10")

    if r.status_code == 200:
        data = r.json()
        df = pd.DataFrame(data["content"])
        st.dataframe(df)
    else:
        st.error("Failed to fetch orders")
