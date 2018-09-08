package storeDiscount;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BillableAmount {

	private Float amount;

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public static void main(String[] args) {
		BillableAmount billableAmount = new BillableAmount();
		Set<User> users = new HashSet<>();
		User affiliatedUser = new AffiliatedUser("Ravi", new Date(), "7894561230"); // this is affiliated user
		User affiliatedUser1 = new AffiliatedUser("RaviC", new Date(), "7894561230"); // this is affiliated user
		User employee = new Employee("Ram", new Date(), "7894561230"); // this is employee user
		User user = new User("Ravi", new Date(), "7894561230"); // this is normal user
		users.add(user);
		users.add(employee);
		users.add(affiliatedUser);
		users.add(affiliatedUser1);
		Goods goods1 = new Goods(GoodsCategory.GROCERIES, 980F);
		Goods goods2 = new Goods(GoodsCategory.OTHERS, 10F);
		List<Goods> goods = new ArrayList<>();
		goods.add(goods2);
		goods.add(goods1);
		billableAmount.getBillableAmount(user, goods);
		billableAmount.getBillableAmount(affiliatedUser, goods);
		billableAmount.getBillableAmount(employee, goods);
	}

	private void getBillableAmount(User user, List<Goods> goods) {
		int discount = 0;
		if(user instanceof Employee) {
			discount = Employee.DISCOUNT;
			System.out.println("discount applied for employee is " + discount);
		} else if (user instanceof AffiliatedUser) {
			discount = AffiliatedUser.DISCOUNT;
			System.out.println("discount applied for AffiliatedUser is " + discount);
		} else {
			Calendar cal = Calendar.getInstance();
			int currentYear = cal.getWeekYear();
			cal.setTime(user.memberShipTakenOn);
			int joinedYear = cal.getWeekYear();
			System.out.println("current year " + currentYear + "  joining year " + joinedYear + " for normal user");
			if (currentYear - joinedYear >= 2) {
				discount = 5;
				System.out.println("discount applied for normal user is " + discount);
			}
		}
		Float totalAmount = 0F;
		Float billableAmount = 0F;
		int no = 0;
		for (Goods good : goods) {
			no++;
			if(GoodsCategory.OTHERS.equals(good.category)) {
				totalAmount = totalAmount + good.price;
				billableAmount = billableAmount + (good.price - good.price * discount / 100);
				System.out.println("Goods category " + GoodsCategory.OTHERS.toString()
				    + ", Price " + good.price + ", discount applied " + discount + "%");
				System.out.println("totalAmount = $" + totalAmount + " billableAmount = $" + billableAmount);
			} else {
				totalAmount = totalAmount + good.price;
				billableAmount = billableAmount + good.price;
				System.out.println("Goods category " + GoodsCategory.GROCERIES.toString()
			    + ", Price " + good.price + ", discount applied " + 0 + "%");
				System.out.println("totalAmount = $" + totalAmount + " billableAmount = $" + billableAmount);
			}
			System.out.println("----------------- billable Amount for " + no + " no of goods is $"+ billableAmount);
		}
		int perHun = (int) (billableAmount / 100);
		billableAmount = billableAmount - 5 * perHun;
		System.out.println("----------------- final billable Amount is " + billableAmount);
	}

}

class Employee extends User {
	public Employee(String name, Date joingDate, String mobileNo) {
		super(name, joingDate, mobileNo);
	}

	public static final int DISCOUNT = 30;
}

class AffiliatedUser extends User {
	public AffiliatedUser(String name, Date memberShipTakenOn, String mobileNo) {
		super(name, memberShipTakenOn, mobileNo);
	}

	public static final int DISCOUNT = 10;
}

class User {
	String name;
	Date memberShipTakenOn;
	String mobileNo;

	public User(String name, Date memberShipTakenOn, String mobileNo) {
		this.name = name;
		this.memberShipTakenOn = memberShipTakenOn;
		this.mobileNo = mobileNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mobileNo == null) ? 0 : mobileNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (mobileNo == null) {
			if (other.mobileNo != null)
				return false;
		} else if (!mobileNo.equals(other.mobileNo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + " " + memberShipTakenOn + " " + mobileNo + "\n";
	}
	
}

class Goods {
	GoodsCategory category;
	Float price;

	public Goods(GoodsCategory category, Float price) {
		this.category = category;
		this.price = price;
	}
}

enum GoodsCategory {
	GROCERIES, OTHERS;
}