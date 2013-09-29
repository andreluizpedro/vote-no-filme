 select                                       
   filme.id as id,                            
   filme.titulo as titulo,                    
   filme.capa as capa,                        
   count(usuario) as votos 
 from Filme filme 
 left join filme.usuarios usuario
 where
 	usuario =:usuario                              
 group by                                     
   id,                                        
   titulo,                                    
   capa                                       
 order by votos desc, id                      