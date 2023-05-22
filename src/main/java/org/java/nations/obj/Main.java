package org.java.nations.obj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
			
			String url = "jdbc:mysql://localhost:3306/nations";
			String user = "root";
			String password = "root";
			
			List<Integer> idList = new ArrayList<>();
			
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
								
								idList.add(countryId);
								hasResults = true;
							
							}
							if (!hasResults) {
		                        System.out.println("Nessuna nazione trovata corrispondente alla ricerca.");
		                   }
					   } catch (SQLException ex) {
						System.err.println("Error");
					   }
						
					   System.out.println("Inserisci l'ID di una nazione: ");
			           int selectedCountryId = scanner.nextInt();
			           scanner.nextLine(); 
			           
			           if (idList.contains(selectedCountryId)) {
			        	   List<String> languagesList = new ArrayList<>();
			        	   String languages = "SELECT languages.language "
									+ "FROM countries "
									+ "JOIN country_languages "
									+ "ON countries.country_id = country_languages.country_id "
									+ "JOIN languages\r\n"
									+ "ON country_languages.language_id = languages.language_id "
									+ "WHERE countries.country_id = ?";

							try(PreparedStatement ps2 = con.prepareStatement(languages)){

								ps2.setInt(1, selectedCountryId);

								try(ResultSet rs2 = ps2.executeQuery()){

									while(rs2.next()) {
										final String language = rs2.getString(1);
										languagesList.add(language);
									}
								}
							} catch(SQLException ex) {
								System.out.println("Query Error");
							}
							
							 System.out.println("Lingue parlate: ");
							 for (String language : languagesList) {
							 System.out.println(language);
						    }
						} else {
							System.out.println("Id not included ");
						}

					} catch (SQLException ex) {
						System.err.println("Query Error");
					}
					

			} catch (SQLException ex){
				System.err.println("Connection error");
			}
	}
}

