module.exports = {
  content: ["./src/main/resources/**/*.{html,js}",
    "./node_modules/flowbite/**/*.js"],
  safelist: [
    'underline'
  ],
  theme: {
    extend: {},
    container: {
      center: true,
    }
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('flowbite/plugin'),
  ]
}
