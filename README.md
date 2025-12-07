IFSP_Anote Sistema Distribuído

  

VPC:


Criar a VPC


Configurar a VPC com:


2 zonas de disponibilidade (east-1a, east-1b)
2 subnets públicas
2 subnets privadas(lembrar de customizar CIDR)
Configurar NAT gateway para In 1 AZ
Definir endpoints para 0
Habilitar DNS hostnames e DNS resolution
*colocar foto da preview do painel*
















S3:
De o nome
desative Block all public access
Crie o S3 com o resto das configurações padrões
Exporte o .jar do build do java para dentro do S3
Exporte o .zip do build do react para dentro do S3




RDS:


Grupo de segurança:
Ir em VPC e selecionar Security Group


Nome, Descrição e selecionar a VPC criada


Adicionar regra e definir:
Tipo: MySQL/Aurora
Source: Web Security Group


Criar


Grupo de Subnet do Banco de dados:


Em RDS selecionar Subnet groups, em seguida criar


Definir Nome, Descrição e selecionar a VPC
Em Adicionar subnets selecionar as zonas de disponibilidade(east-1a, east-1b)
Expandir a lista de valores e selecionar as subnets associadas aos valores de CIDR


Criar


Instância RDS:


Selecionar criar database
Selecionar  MySQL nas opções de Engine
Dev/Test como Template
Selecionar Multi-AZ DB instance
Definir id, username, e senha
Configurar Burstable classes e selecionar db.t3.micro


Em storage configurar:


Tipo como General Purpose e alocar 20
Em conectividade selecionar a VPC
Selecionar o Grupo de segurança criado em Existing VPC security groups
Deixar configurações de monitoramento, backups criptografia ativas


Criar.




Auto Scaling e Load Balancer:
Load Balancer:


Selecionar Target Groups em EC2
Criar target group
Tipo: instância
Definir nome e selecionar a VPC
Selecionar Próximo >> Criar target group >> criar load balancer
Abaixo de application load balancer selecionar Create
Definir o nome


Em networking mapping:


Selecionar a VPC do projeto e as subnets do projeto
Selecionar as subnets desejadas
Subnet publica 1 e  Subnet publica 2
Selecionar o security group próprio


Criar




Launch template e auto scaling group


Selecionar Launch Templates em EC2
Criar launch template
Definir nome
Selecionar tipo instância t3.micro
Em chaves selecionar a sua chave criada
Em Firewall selecionar existing security group
Selecionar o grupo de segurança das sua subnet privada que não seja o do banco de dados
Selecionar Enable em Detailed CloudWatch monitoring
colar o código abaixo no user data do Advanced details

#!/bin/bash


# 1. Atualiza o sistema e instala Java e Nginx
yum update -y
yum install java-17-amazon-corretto nginx unzip -y


# 2. Prepara diretórios
mkdir -p /var/www/html/react
mkdir -p /app/backend


# 3. Baixa os artefatos do S3
aws s3 cp s3://ifsp-anote-bucket/frontend.zip /tmp/frontend.zip
aws s3 cp s3://ifsp-anote-bucket/backend.jar /app/backend/app.jar


# 4. Configura o Frontend (React)
# Descompacta o site estático na pasta que o Nginx vai ler
unzip /tmp/frontend.zip -d /var/www/html/react
# Dá permissão para o Nginx ler os arquivos
chown -R nginx:nginx /var/www/html/react
chmod -R 755 /var/www/html/react


# 5. Cria o serviço do Spring Boot (systemd)
# Isso faz o Java rodar em segundo plano e reiniciar se cair
cat <<EOF > /etc/systemd/system/myapp.service
[Unit]
Description=Spring Boot App
After=syslog.target


[Service]
User=root
ExecStart=/usr/bin/java -jar /app/backend/app.jar
SuccessExitStatus=143
Restart=always


[Install]
WantedBy=multi-user.target
EOF


# Inicia o Java
systemctl enable myapp
systemctl start myapp


# 6. Configura o Nginx (O Proxy Reverso)
# Apaga a config padrão e cria a nossa
rm /etc/nginx/nginx.conf


cat <<EOF > /etc/nginx/nginx.conf
user nginx;
worker_processes auto;
error_log /var/log/nginx/error.log;
pid /run/nginx.pid;


events {
    worker_connections 1024;
}


http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;
    log_format  main  '\$remote_addr - \$remote_user [\$time_local] "\$request" '
                      '\$status \$body_bytes_sent "\$http_referer" '
                      '"\$http_user_agent" "\$http_x_forwarded_for"';
    access_log  /var/log/nginx/access.log  main;
    sendfile        on;
    keepalive_timeout  65;


    server {
        listen       80;
        server_name  _;


        # Rota 1: Tudo que NÃO for /api vai para o React
        location / {
            root   /var/www/html/react;
            index  index.html index.htm;
            # Importante para React Router (SPA): Se não achar o arquivo, serve o index.html
            try_files \$uri \$uri/ /index.html; 
        }


        # Rota 2: Tudo que começar com /api vai para o Java (Spring Boot)
        location /api {
            proxy_pass http://localhost:8080; # Manda para o Java rodando na porta 8080
            proxy_set_header Host \$host;
            proxy_set_header X-Real-IP \$remote_addr;
            proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        }
    }
}
EOF


# 7. Inicia o Nginx
systemctl enable nginx
systemctl start nginx

escolher o IAM instance profile, que permita visualizar o S3
Criar launch template
Selecionar o launch template e em seguida actions


Na primeira etapa:


Definir nome
Conferir se o template criado está selecionado
Selecionar Next


Segunda etapa:


Selecionar a VPC do projeto
Em Availability Zones and Subnets selecionar as duas Subnets privadas
Selecionar Next


terceira Etapa:


Selecionar Attach to an existing load balancer
Selecionar o target group


Em additional settings
Habilitar Enable group metrics collection within CloudWatch
Selecionar Next


Quarta Etapa:


Abaixo de Group size:
Capacidade desejada: 2
Capacidade mínima: 2
Capacidade máxima: 4


Abaixo de Scaling policies:
Definir nome
Tipo de metrica: Avarege CPU Utilization
Target value: 60
Selecionar Next


Etapa 5
Configurações padrão
Selecionar Next


Etapa 6
Apenas tags
Selecionar Next


Etapa 7
Conferir detalhes
Criar Auto Scaling Group
















COMENTÁRIOS E OBSERVAÇÕES DA IMPLEMENTAÇÃO
________________


* Roteamento (Proxy Nginx): A configuração de location /api roteia todo o tráfego de API para o Spring Boot. Isso simplifica a aplicação, que não precisa conhecer o prefixo /api. O restante do tráfego é direcionado para o index.html do React (necessário para Single Page Applications - SPAs).
* Alta Disponibilidade (HA): O uso de Multi-AZ para RDS e as 2 Subnets Privadas no ASG/ALB garantem que o sistema continue operacional mesmo se uma Zona de Disponibilidade falhar.
* Isolamento de Segurança: O backend (EC2) e o banco de dados (RDS) rodam nas Subnets Privadas. O tráfego da internet só pode acessar o ALB (Subnets Públicas), que atua como o único ponto de entrada.
* Dificuldade Comum: A principal dificuldade em ambientes como este é a configuração do SELinux ou do firewall (httpd_can_network_connect), que deve ser habilitado para permitir que o Nginx se comunique com o Spring Boot na porta 8080. ================================================================================


Auto Scaling: é um serviço que ajusta automaticamente a quantidade de servidores EC2 conforme a demanda, aumentando quando o uso cresce e reduzindo quando o tráfego diminui, garantindo desempenho adequado e economia de custos sem intervenção manual.
VPC (Virtual Private Cloud): é uma rede virtual isolada dentro da AWS onde você controla endereços IP, sub-redes, rotas e segurança, funcionando como seu próprio datacenter privado na nuvem para hospedar recursos como EC2, bancos de dados e serviços internos.
Load Balancer: distribui automaticamente o tráfego entre múltiplas instâncias ou serviços, garantindo que nenhuma fique sobrecarregada, aumentando a disponibilidade e confiabilidade da aplicação ao direcionar usuários para servidores saudáveis e com menor carga.
RDS (Relational Database Service): é um serviço gerenciado de banco de dados que automatiza tarefas como backup, atualização e replicação, permitindo que você use bancos relacionais como MySQL, PostgreSQL ou SQL Server sem precisar administrar servidores manualmente.
A EC2 (Elastic Compute Cloud): é um serviço da AWS que fornece servidores virtuais sob demanda, permitindo executar aplicações, controlar totalmente o ambiente, ajustar recursos conforme a necessidade e integrar facilmente com outros serviços da nuvem.
S3 (Simple Storage Service): é um serviço de armazenamento na nuvem que guarda arquivos de forma escalável, segura e altamente disponível, permitindo armazenar desde imagens e documentos até backups e grandes volumes de dados acessíveis pela internet ou outras aplicações AWS.
