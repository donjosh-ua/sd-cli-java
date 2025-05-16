from kafka import KafkaProducer
import json
import socket


def test_kafka_connection():
    """Test basic connectivity to Kafka broker"""
    print("Testing connection to Kafka broker...")
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(2)
        s.connect(("192.168.18.157", 9092))
        s.close()
        print("✓ TCP connection to Kafka broker successful!")
        return True
    except Exception as e:
        print(f"✗ Failed to connect to Kafka broker: {e}")
        return False


def json_serializer(data):
    """Serialize data to JSON bytes"""
    return json.dumps(data).encode("utf-8")


def send_expense_registration(plan_id, username, expense_amount):
    """Send an expense registration request to Kafka"""
    if not test_kafka_connection():
        print("Cannot establish connection to Kafka broker. Exiting.")
        return False

    print(
        f"Sending expense registration: User={username}, Plan={plan_id}, Amount={expense_amount}"
    )

    payload = {"planId": plan_id, "username": username, "expense": expense_amount}

    try:
        producer = KafkaProducer(
            bootstrap_servers=["192.168.18.157:9092"], value_serializer=json_serializer
        )
        producer.send("register_expense", payload)
        producer.flush()
        producer.close()
        print("✓ Message sent successfully!")
        return True
    except Exception as e:
        print(f"✗ Error sending message: {e}")
        return False


if __name__ == "__main__":
    # Example: Send an expense registration
    send_expense_registration(
        plan_id=2,  # Replace with actual plan ID
        username="jochua",  # Replace with actual username
        expense_amount=25.75,
    )
