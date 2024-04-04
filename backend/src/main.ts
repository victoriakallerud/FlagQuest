import { NestFactory } from '@nestjs/core';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { AppModule } from './app.module';
import { ValidationPipe, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import * as admin from 'firebase-admin';
import { applicationDefault } from 'firebase-admin/app';

async function bootstrap() {
  admin.initializeApp({
    credential: applicationDefault(),
    databaseURL: "https://flagquest-d4502-default-rtdb.europe-west1.firebasedatabase.app",
  });
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(new ValidationPipe());

  const logger = new Logger('bootstrap');

  const configService = app.get(ConfigService);
  const currentKeyPath = configService.get<string>('GOOGLE_APPLICATION_CREDENTIALS');
  if(!currentKeyPath) {
    logger.error('GOOGLE_APPLICATION_CREDENTIALS is not set');
    process.exit(1);
  } else {
    logger.log(`Using follwing Google credentials filepath: ${currentKeyPath}`);
  }
  try{
    admin.firestore().listCollections();
  } catch(error) {
    logger.error('Could not connect to Firestore');
    logger.error(error.message);
    process.exit(1);
  }

  const config = new DocumentBuilder()
    .setTitle('FlagQuest Backend')
    .setDescription('The API for the FlagQuest guessing game')
    .setVersion('1.2')
    .addTag('user')
    .addTag('lobby')
    .build();
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('docs', app, document);

  await app.listen(3000);
}
bootstrap();
