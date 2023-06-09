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
					catch(Exception ex) { 
						System.err.println(ex);
					}
				}
					
					
						System.out.print("Id nazione: ");
						final String strIdNation = scanner.nextLine();
						final int idNation = Integer.valueOf(strIdNation);
						
						System.out.println("");
						
						String subSql1 = " SELECT l.`language` "
										+ " FROM country_languages cl "
										+ "	JOIN languages l "
										+ "		ON cl.language_id = l.language_id "
										+ " WHERE cl.country_id = ?; ";
						String subSql2 = " SELECT c.name, cs.* "
										+ " FROM countries c "
										+ "	JOIN country_stats cs "
										+ "		ON c.country_id = cs.country_id "
										+ " WHERE cs.country_id = ? "
										+ " ORDER BY cs.`year` DESC "
										+ " LIMIT 1; ";
						
						try (PreparedStatement ps1 = con.prepareStatement(subSql1);
								PreparedStatement ps2 = con.prepareStatement(subSql2);) {
							
							ps1.setInt(1, idNation);
							ps2.setInt(1, idNation);
							
							try (ResultSet rs1 = ps1.executeQuery();
									ResultSet rs2 = ps2.executeQuery()) {
								if (!rs2.next()) return;
								
								String nationName = rs2.getString(1);
								
								System.out.println("Details for country: " + nationName);
								System.out.print("Languages: ");
								
								while(rs1.next()) {
									
									System.out.print(rs1.getString(1) 
											+ (rs1.isLast() ? "" : ", "));
								}
								
								System.out.println("\nMost recent stats");
								System.out.println("Year: " + rs2.getInt(3));
								System.out.println("Population: " + rs2.getInt(4));
								System.out.println("GDP: " + rs2.getLong(5));
							} catch(Exception ex) { 
								System.err.println(ex);
							}
						} catch(Exception ex) { 
							System.err.println(ex);
						}
					

			} catch (SQLException ex){
				System.err.println("Connection error");
			}
	}
}

