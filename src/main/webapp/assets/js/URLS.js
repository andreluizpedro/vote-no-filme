var URLS = new (function () {
    var self = this;
    self.base = 'voto/';
    self.carregaDoisFilmes = self.base + 'carregaDoisFilmes';
    self.votar = function (data) {
        return  self.base + 'votar/' + data.id + '/' + data.naoVotadoId;
    };
    self.resetaVotacao = self.base + 'resetaVotacao';
    self.confirmaVotacao = self.base + 'confirmaVotacao';
    self.carregaRanking = self.base + 'carregaRanking';
    return this;
})();