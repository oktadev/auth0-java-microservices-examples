const { pathsToModuleNameMapper } = require('ts-jest');
const config = require('../../../webpack/config');
const {
  compilerOptions: { paths = {}, baseUrl = './' },
} = require('../../../tsconfig.json');

/** @type {import('ts-jest').InitialOptionsTsJest} */
module.exports = {
  rootDir: '../../../',
  roots: ['<rootDir>', `<rootDir>/${baseUrl}`, `<rootDir>/src/test/javascript/spec/app/`],
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  coverageDirectory: '<rootDir>/build/test-results/',
  coveragePathIgnorePatterns: [
    '<rootDir>/node_modules/',
    '<rootDir>/src/test/javascript/',
    '<rootDir>/src/main/webapp/app/router',
    '.*.json',
  ],
  moduleFileExtensions: ['js', 'json', 'ts', 'vue'],
  transform: {
    '.*\\.(vue)$': 'vue-jest',
    '^.+\\.tsx?$': [
      'ts-jest',
      {
        tsconfig: '<rootDir>/tsconfig.spec.json',
      },
    ],
  },
  moduleNameMapper: {
    rxjs: '<rootDir>/node_modules/rxjs/dist/bundles/rxjs.umd.js',
    '^@/(.*)$': '<rootDir>/src/main/webapp/app/$1',
    ...pathsToModuleNameMapper(paths, { prefix: `<rootDir>/${baseUrl}/` }),
  },
  reporters: [
    'default',
    ['jest-junit', { outputDirectory: './build/test-results/', outputName: 'TESTS-results-jest.xml' }],
    ['jest-sonar', { outputDirectory: './build/test-results/jest', outputName: 'TESTS-results-sonar.xml' }],
  ],
  testMatch: ['<rootDir>/src/test/javascript/spec/**/@(*.)@(spec.ts)'],
  testEnvironmentOptions: {
    url: 'https://jhipster.tech',
  },
  snapshotSerializers: ['jest-serializer-vue'],
  globals: {
    I18N_HASH: 'generated_hash',
    SERVER_API_URL: config.serverApiUrl,
    VERSION: config.version,
  },
  coverageThreshold: {
    global: {
      statements: 80,
      branches: 60,
      functions: 70,
      lines: 80,
    },
  },
};
