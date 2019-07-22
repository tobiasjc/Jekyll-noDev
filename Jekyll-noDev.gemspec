# frozen_string_literal: true

Gem::Specification.new do |spec|
  spec.name          = "Jekyll-noDev"
  spec.version       = "0.1.0"
  spec.authors       = ["José Tobias"]
  spec.email         = ["jose.tobias@outlook.com"]

  spec.summary       = "A theme for professional looking extensible personal portfolios."
  spec.homepage      = "https://github.com/tobiasjc/Jekyll-noDev"
  spec.license       = "MIT"

  spec.files         = `git ls-files -z`.split("\x0").select { |f| f.match(%r!^(assets|_layouts|_includes|_sass|LICENSE|README)!i) }

  spec.add_runtime_dependency "jekyll", "~> 3.8"

  spec.add_development_dependency "bundler", "~> 2.0.2"
  spec.add_development_dependency "rake", "~> 12.0"
end
