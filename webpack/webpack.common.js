const path = require('path');
const webpack = require('webpack');
const { merge } = require('webpack-merge');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');
const utils = require('./utils.js');
const environment = require('./environment');

const getTsLoaderRule = env => {
  const rules = [
    {
      loader: 'thread-loader',
      options: {
        workers: require('os').cpus().length - 1,
      },
    },
    {
      loader: 'ts-loader',
      options: {
        transpileOnly: true,
        happyPackMode: true,
      },
    },
  ];
  return rules;
};

module.exports = async options => {
  const development = options.env === 'development';
  return merge({
    cache: {
      type: 'filesystem',
      cacheDirectory: path.resolve(__dirname, '../target/webpack'),
      buildDependencies: {
        config: [
          __filename,
          path.resolve(__dirname, `webpack.${development ? 'dev' : 'prod'}.js`),
          path.resolve(__dirname, 'environment.js'),
          path.resolve(__dirname, 'utils.js'),
          path.resolve(__dirname, '../postcss.config.js'),
          path.resolve(__dirname, '../tsconfig.json'),
        ],
      },
    },
    resolve: {
      extensions: ['.js', '.jsx', '.ts', '.tsx', '.json'],
      modules: ['node_modules'],
      alias: utils.mapTypescriptAliasToWebpackAlias(),
      fallback: {
        path: require.resolve('path-browserify'),
      },
    },
    module: {
      rules: [
        {
          test: /\.tsx?$/,
          use: getTsLoaderRule(options.env), // Use the 'use' property correctly
          include: [utils.root('./src/main/webapp/app')],
          exclude: [utils.root('node_modules')],
        },
        /*
       ,
       Disabled due to https://github.com/jhipster/generator-jhipster/issues/16116
       Can be enabled with @reduxjs/toolkit@>1.6.1
      {
        enforce: 'pre',
        test: /\.jsx?$/,
        loader: 'source-map-loader'
      }
      */
      ],
    },
    stats: {
      children: false,
    },
    plugins: [
      new webpack.EnvironmentPlugin({
        LOG_LEVEL: development ? 'info' : 'error',
      }),
      new webpack.DefinePlugin({
        DEVELOPMENT: JSON.stringify(development),
        VERSION: JSON.stringify(environment.VERSION),
        SERVER_API_URL: JSON.stringify(environment.SERVER_API_URL),
      }),
      new ESLintPlugin({
        baseConfig: {
          parserOptions: {
            project: ['../tsconfig.json'],
          },
        },
      }),
      new ForkTsCheckerWebpackPlugin(),
      new CopyWebpackPlugin({
        patterns: [
          {
            context: require('swagger-ui-dist').getAbsoluteFSPath(),
            from: '*.{js,css,html,png}',
            to: 'swagger-ui/',
            globOptions: { ignore: ['**/index.html'] },
          },
          {
            from: path.join(path.dirname(require.resolve('axios/package.json')), 'dist/axios.min.js'),
            to: 'swagger-ui/',
          },
          { from: './src/main/webapp/swagger-ui/', to: 'swagger-ui/' },
          { from: './src/main/webapp/content/', to: 'content/' },
          { from: './src/main/webapp/favicon.ico', to: 'favicon.ico' },
          { from: './src/main/webapp/manifest.webapp', to: 'manifest.webapp' },
          { from: './src/main/webapp/robots.txt', to: 'robots.txt' },
        ],
      }),
      new HtmlWebpackPlugin({
        template: './src/main/webapp/index.html',
        chunksSortMode: 'auto',
        inject: 'body',
        base: '/',
      }),
    ],
  });
};
