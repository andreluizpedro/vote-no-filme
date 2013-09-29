select filme, filme.usuarios.size as voto_ from Filme filme
group by filme
order by voto_ desc