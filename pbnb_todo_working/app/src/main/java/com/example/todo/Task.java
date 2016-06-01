package com.example.todo;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable{
	
private static int count=0;
Integer id;
String description;
String owner;
String updatetime;
Boolean status;


public Task(String description, String owner, String updatetime, Boolean status) {
	super();
	this.id=++count;
	this.description = description;
	this.owner = owner;
	this.updatetime = updatetime;
	this.status = status;
}
public Task(Integer id,String description, String owner, String updatetime, Boolean status) {
	super();
	this.id=id;
	this.description = description;
	this.owner = owner;
	this.updatetime = updatetime;
	this.status = status;
}

public Task(Parcel parcel) {
	this.id=parcel.readInt();
	this.description = parcel.readString();
	this.owner = parcel.readString();
	this.updatetime=parcel.readString();
	if (parcel.readByte()==1) status =true;
	else status=false; ;
}

public Integer getId() {
	return id;
}

public static int getCount() {
	return count;
}

public void setId(Integer id) {
	this.id = id;
}

public String getDescription() {
	return description;
}
public static void setCount(Integer cnt) {
	Task.count=cnt;
}


public void setDescription(String description) {
	this.description = description;
}
public String getOwner() {
	return owner;
}
public void setOwner(String owner) {
	this.owner = owner;
}
public String getUpdatetime() {
	return updatetime;
}
public void setUpdatetime(String updatetime) {
	this.updatetime = updatetime;
}
public Boolean getStatus() {
	return status;
}
public void setStatus(Boolean status) {
	this.status = status;
}@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}

@Override
public void writeToParcel(Parcel out, int flags) {
	out.writeInt(id);
	out.writeString(description);
	out.writeString(owner);
	out.writeString(updatetime);
	
	out.writeByte((byte) (status ? 1 : 0));     //if status == true, byte == 1

}

public static final Creator<Task> CREATOR = new Creator<Task>() {

    // And here you create a new instance from a parcel using the first constructor
    @Override
    public Task createFromParcel(Parcel parcel) {
        return new Task(parcel);
    }

    @Override
    public Task[] newArray(int size) {
        return new Task[size];
    }
};


@Override
public String toString() {
	return "Task [id="+id+", description=" + description + ", owner=" + owner
			+ ", updatetime=" + updatetime + ", status=" + status + "]";
}

}
