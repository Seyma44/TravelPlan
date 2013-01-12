package com.dabdm.travelplan.places;

public class PlaceDetails {
	public String international_phone_number;
	public OpeningHours opening_hours;
	public Photo[] photos;
	public class Photo {
		public String photo_reference;
		}
	public class OpeningHours {
		public boolean open_now; 
		public Periods[] periods;
	}
	public class Periods {
		public Close close;
		public Open open;
	}
	public class Open {
		public int day;
		public String time;
	}
	public class Close {
		public int day;
		public String time;
	}
	
}
