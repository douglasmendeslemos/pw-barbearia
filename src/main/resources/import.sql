-- Limpa os serviços existentes
TRUNCATE TABLE servicos RESTART IDENTITY CASCADE;

-- Insere 4 serviços únicos e condizentes com o contexto de barbearia
INSERT INTO servicos (nome, descricao, valor, duracaoMinutos) VALUES 
('Corte Masculino Premium', 'Corte moderno com lavagem, finalização com pomada e massagem capilar.', 50.00, 40),
('Barba de Respeito', 'Modelagem de barba com terapia de toalha quente, óleos essenciais e pós-barba premium.', 35.00, 30),
('Combo BD+ (Cabelo & Barba)', 'Nosso serviço completo: corte premium e barba com toalha quente com desconto exclusivo.', 75.00, 70),
('Tratamento Capilar & Escova', 'Hidratação profunda dos fios, lavagem refrescante e secagem com modelagem.', 40.00, 30);