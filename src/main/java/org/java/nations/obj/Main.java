package org.java.nations.obj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
			
			String url = "jdbc:mysql://localhost:3306/nations";
			String user = "root";
			String password = "root";
			
				try(Connection con = DriverManager.getConnection(url, user, password)) {
					
					 Scanner scanner = new Scanner(System.in);
			         System.out.println("Inserisci una stringa di ricerca: ");
			         String searchString = scanner.nextLine();
			    
					String sql = "SELECT countries.name, countries.country_id, regions.name, continents.name "
						+ "FROM countries "
						+ "JOIN regions "
						+ "ON countries.region_id = regions.region_id "
					    + "JOIN continents "
						+ "ON regions.continent_id = continents.continent_id "
						+ "WHERE countries.name LIKE ? " 
						+ "ORDER BY countries.name ";
				
				
					try (PreparedStatement ps = con.prepareStatement(sql)) {
						ps.setString(1, "%" + searchString + "%");
											
						try (ResultSet rs = ps.executeQuery()) {
							
							System.out.println("Nazione - ID - Regione - Continente");
							
						    boolean hasResults = false;
							while(rs.next()) {
								
								final String countryName = rs.getString(1);
								final int countryId = rs.getInt(2);
								final String regionName = rs.getString(3);
								final String continentName = rs.getString(4);
								
								System.out.println(countryName + " - " + countryId + " - " 
										+ regionName + " - " + continentName);
								
								hasResults = true;
							
							}
							if (!hasResults) {
		                        System.out.println("Nessuna nazione trovata corrispondente alla ricerca.");
		                   }
					   } catch (SQLException ex) {
						System.err.println("Error");
					   }
						
					} catch (SQLException ex) {
						System.err.println("Query Error");
					}

			} catch (SQLException ex){
				System.err.println("Connection error");
			}
	}
}

