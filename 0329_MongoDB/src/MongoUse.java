import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListDatabasesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;

public class MongoUse {

	public static void main(String[] args) {
		//몽고 디비에 접속하기 위한 정보
		String ip = ""; //문자열 가지고 연산을 하는 경우 => +
		String db_name = null; //문자열을 대입받는 경우
		int port = -1;
		
		//텍스트 파일의 내용을 읽기 - BufferedReader
		try(BufferedReader br = 
				new BufferedReader(
					new InputStreamReader(
							new FileInputStream(
							 new File("./db.txt"))))){
			//한줄의 텍스트 읽기
			String line = br.readLine();
			System.out.printf("%s\n",line);
			//공백을 기준으로 분할하기
			//특정 패턴을 가지고 분할 : String [] split(String pattern)
			String []ar = line.split(" ");
			ip = ar[0];
			port = Integer.parseInt(ar[1]);
			db_name = ar[2];
			
			
			for(String p : ar) {
				System.out.printf("%s\n",p);
			}
			
			
			
			
		}catch(Exception e) {
			System.out.printf("%s\n", e.getMessage());
			e.printStackTrace();
		}
		
		
		
		//Mongo DB 접속
		//MongoClient 클래스를 이용 - 생성자에 new ServerAddress	(ip,port) 설정
		//1.접속한 후 listDatabases()라는 메소드를 호출하면 Iterator가 구현되 객체가 리턴
		MongoClient client = new MongoClient(new ServerAddress(ip,port));
		ListDatabasesIterable<Document> dbs = client.listDatabases();
		for(Document document : dbs) {
			System.out.printf("%s\n", document);
		}
		//리턴 받은 결과를 출력
		
		
		//2.접속한 후 getDatabase(데이터베이스이름)를 호출하면 데이터베이스를 사용할 수 있는 
		//객체가 리턴됩니다.
		MongoDatabase db = client.getDatabase(db_name);
		//3.위의 객체를 가지고 listCollectionNames()를 호출하면 모든 컬렉션의 이름이 
		//리턴됩니다.
		MongoIterable<String> dbNames = db.listCollectionNames();
		
		//컬렉션 이름을 모두 출력
		for(String name : dbNames) {
			System.out.printf("%s\n", name);
		}
		
		//users 컬렉션 가져오기
		MongoCollection<Document> users =db.getCollection("users");
		//기록을 하기 위해서 쓰기 권한을 가져오기
		users.getWriteConcern();
		//기록을 하기 위해서는 Document 객체에 데이터를 설정 -Map
		//컬렉션 객체가 insertOne 또는 insertMay를 호출
		Document document = new Document();
		document.put("id","ggangpae1");
		document.put("password", "12345687");
		document.put("name","박문석");
		
		
		 users.insertOne(document);
		
		//users의 전체 데이터 읽기
		/* 
		 FindIterable<Document> result = users.find();
		 for(Document temp : result) {
			 System.out.printf("%s\n",temp);
		 }
		*/
		 
		//조건에 맞는 데이터만 읽어오기
		 FindIterable<Document> result = users.find(Filters.eq("id","root"));
		 for(Document temp : result) {
			 System.out.printf("%s\n",temp);
		 }
		//4.사용이 끝나면 MongoClient.close()
		client.close();

	}

}
