from kafka import KafkaConsumer, KafkaProducer
import json
import socket
from datetime import datetime


class ExpenseNotification:
    def __init__(self, data):
        self.expense_id = data.get("expenseId")
        self.expense_name = data.get("expenseName")
        self.amount = data.get("amount")
        self.date = data.get("date")
        self.type = data.get("type")
        self.user_id = data.get("userId")
        self.username = data.get("username")
        self.plan_id = data.get("planId")
        self.plan_name = data.get("planName")
        self.total_plan_expense = data.get("totalPlanExpense")
        self.affected_user_ids = data.get("affectedUserIds", [])

    def __str__(self):
        return f"Expense: {self.expense_name}, Amount: {self.amount}, Plan: {self.plan_name}"

    @classmethod
    def create(
        cls,
        expense_name,
        amount,
        expense_type,
        user_id,
        username,
        plan_id=None,
        plan_name=None,
        date=None,
        affected_user_ids=None,
    ):
        """Create a new expense notification"""
        if date is None:
            date = datetime.now().strftime("%Y-%m-%dT%H:%M:%S")

        data = {
            "expenseId": None,  # Generated on server side
            "expenseName": expense_name,
            "amount": amount,
            "date": date or datetime.now().strftime("%Y-%m-%dT%H:%M:%S"),
            "type": expense_type,
            "userId": user_id,
            "username": username,
            "planId": plan_id,
            "planName": plan_name,
            "totalPlanExpense": None,  # Calculated on server side
            "affectedUserIds": affected_user_ids or [],
        }

        if plan_id and plan_name:
            data["planId"] = plan_id
            data["planName"] = plan_name

        if affected_user_ids:
            data["affectedUserIds"] = affected_user_ids

        return cls(data)

    def to_dict(self):
        """Convert to dictionary for serialization"""
        return {
            "expenseId": self.expense_id,
            "expenseName": self.expense_name,
            "amount": self.amount,
            "date": self.date,
            "type": self.type,
            "userId": self.user_id,
            "username": self.username,
            "planId": self.plan_id,
            "planName": self.plan_name,
            "totalPlanExpense": self.total_plan_expense,
            "affectedUserIds": self.affected_user_ids,
        }


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


def json_deserializer(data):
    if data is None:
        return None
    try:
        return json.loads(data.decode("utf-8"))
    except Exception as e:
        print(f"Error deserializing message: {e}")
        print(f"Raw data: {data}")
        return {}


def send_expense_notification(notification):
    """Send an expense notification to Kafka"""
    if not test_kafka_connection():
        print("Cannot establish connection to Kafka broker. Exiting.")
        return False

    print(f"Sending expense notification: {notification.expense_name}")

    try:
        producer = KafkaProducer(
            bootstrap_servers=["192.168.18.157:9092"], value_serializer=json_serializer
        )
        producer.send("join_plan", notification.to_dict())
        producer.flush()
        producer.close()
        return True
    except Exception as e:
        print(f"✗ Error sending message: {e}")
        return False

    except Exception as e:
        print(f"✗ Error sending message: {e}")
        return False


def create_and_send_expense(
    expense_name,
    amount,
    expense_type,
    user_id,
    username,
    plan_id=None,
    plan_name=None,
    affected_user_ids=None,
):
    """Helper function to create and send an expense notification"""
    # Create the notification
    notification = ExpenseNotification.create(
        expense_name=expense_name,
        amount=amount,
        expense_type=expense_type,
        user_id=user_id,
        username=username,
        plan_id=plan_id,
        plan_name=plan_name,
        date=datetime.now().strftime("%Y-%m-%dT%H:%M:%S"),
        affected_user_ids=affected_user_ids,
    )

    # Send the notification
    return send_expense_notification(notification)


def start_kafka_consumer():
    if not test_kafka_connection():
        print("Cannot establish connection to Kafka broker. Exiting.")
        return

    print("Starting Kafka consumer with configuration:")
    print("- Topic: expense-notifications")
    print("- Bootstrap servers: 192.168.18.157:9092")
    print("- Auto offset reset: earliest")
    print("- Group ID: python-expense-client")

    try:
        consumer = KafkaConsumer(
            "join_plan",
            bootstrap_servers=["192.168.18.157:9092"],
            auto_offset_reset="earliest",
            enable_auto_commit=True,
            group_id="python-expense-client",
            value_deserializer=json_deserializer,
            consumer_timeout_ms=30000,  # Adding timeout for testing
        )

        print("Consumer initialized. Available topics:", consumer.topics())

        print("Starting to poll for messages...")
        for message in consumer:
            try:
                print(f"Received raw message: {message}")
                notification = ExpenseNotification(message.value)
                print(f"\n--- New Expense Notification ---")
                print(f"Expense: {notification.expense_name}")
                print(f"Amount: ${notification.amount}")
                print(f"User: {notification.username}")
                print(f"Plan: {notification.plan_name}")
                print(f"Total Plan Expense: ${notification.total_plan_expense}")
                print(f"Affected Users: {len(notification.affected_user_ids)} users")
            except Exception as e:
                print(f"Error processing message: {e}")
                print(f"Message value: {message.value}")
    except Exception as e:
        print(f"Error creating consumer: {e}")
    finally:
        print("Consumer stopped")


# Example of how to use the send function
def send_test_notification():
    """Send a test expense notification"""
    print("Sending test expense notification...")
    notification = ExpenseNotification.create(
        expense_name="Pizza Night",
        amount=45.50,
        expense_type="shared",
        user_id=1,
        username="testuser",
        plan_id=2,
        plan_name="Team Lunch",
        affected_user_ids=[1, 2, 3],
    )

    success = send_expense_notification(notification)
    if success:
        print("Test notification sent successfully!")
    else:
        print("Failed to send test notification.")


if __name__ == "__main__":
    # Uncomment the function you want to run

    # Send a test notification
    send_test_notification()

    # Or start the consumer
    # start_kafka_consumer()

    # Or create and send a specific expense
    # create_and_send_expense(
    #     expense_name="Movie Tickets",
    #     amount=32.50,
    #     expense_type="shared",
    #     user_id=2,
    #     username="sarah",
    #     plan_id=3,
    #     plan_name="Weekend Plans",
    #     affected_user_ids=[2, 3, 4]
    # )
